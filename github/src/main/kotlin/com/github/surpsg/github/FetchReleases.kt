package com.github.surpsg.github

import org.kohsuke.github.GHIssue
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GHPullRequest
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.writeLines

val dateFormat = SimpleDateFormat("yyyy-MM")
val githubToken = System.getenv("GITHUB_TOKEN") ?: throw RuntimeException("Token is not set")

fun main() {
    val repos = listOf(
        "robfletcher/strikt",
        "kotest/kotest",
        "willowtreeapps/assertk",
        "MarkusAmshove/Kluent"
    )
    repos.forEach { repoName ->
        storeReleasesStats(repoName)
        storeCommitsStats(repoName)
        storePrStats(repoName)
        storeIssuesStats(repoName)
    }
}

fun storePrStats(project: String) {
    println("Fetch all PRs of $project")
    val getDate: GHPullRequest.() -> Date = { createdAt }

    val repo: GHRepository = buildGithubClient().getRepository(project)
    val allPullRequests: Iterable<GHPullRequest> = repo.queryPullRequests()
        .state(GHIssueState.ALL)
        .list()
        .toList()
        .sortedBy(getDate)

    allPullRequests.forEach {
        println("\tcreated=${dateFormat.format(it.getDate())}, isMerged=${it.isMerged}, title=${it.title}")
    }

    val mergedKey = "Merged"
    val openKey = "Open"

    val isMergedToDates: Map<String, List<Date>> = allPullRequests.toList().groupBy(
        keySelector = { if (it.isMerged) mergedKey else openKey },
        valueTransform = getDate
    )

    val firstPrDate = allPullRequests.minBy(getDate).getDate()
    val csvData = isMergedToDates
        .map { (status, dateCreated) ->
            val prsByMonth: MutableMap<String, Int> = dateCreated
                .groupingBy {
                    dateFormat.format(it)
                }
                .eachCount()
                .toMutableMap()

            generateDateRange(firstPrDate)
                .associate { dateFormat.format(it) to 0 }
                .forEach { (date, count) -> prsByMonth.putIfAbsent(date, count) }

            status to prsByMonth.toSortedMap()
        }
        .flatMap { (status, dates) ->
            mutableListOf(status)
                .union(
                    dates.map { (date, count) -> "$date,$count" }
                )
        }

    val name: String = project.substringAfter("/")
    val targetPath = Path.of("$name-PR.csv")
    targetPath.writeLines(csvData)
}

fun storeIssuesStats(project: String) {
    println("Fetch all issues of $project")
    val getDate: GHIssue.() -> Date = { createdAt }

    val repo: GHRepository = buildGithubClient().getRepository(project)
    val allIssues: List<GHIssue> = repo.queryIssues().state(GHIssueState.ALL).pageSize(100).list().toList()
        .sortedBy(getDate)

    allIssues.forEach {
        println("\tcreated=${dateFormat.format(it.getDate())}, state=${it.state}, title=${it.title}")
    }

    val isClosedToDates: Map<GHIssueState, List<Date>> = allIssues.toList().groupBy(
        keySelector = { it.state },
        valueTransform = getDate
    )

    val firstIssueDate = allIssues.minBy(getDate).getDate()
    val csvData = isClosedToDates
        .map { (status, dateCreated) ->
            val issueByMonth: MutableMap<String, Int> = dateCreated
                .groupingBy {
                    dateFormat.format(it)
                }
                .eachCount()
                .toMutableMap()

            generateDateRange(firstIssueDate)
                .associate { dateFormat.format(it) to 0 }
                .forEach { (date, count) -> issueByMonth.putIfAbsent(date, count) }

            status to issueByMonth.toSortedMap()
        }
        .flatMap { (status, dates) ->
            mutableListOf(status.name)
                .union(
                    dates.map { (date, count) -> "$date,$count" }
                )
        }

    val name: String = project.substringAfter("/")
    val targetPath = Path.of("$name-issues.csv")
    targetPath.writeLines(csvData)
}

fun storeCommitsStats(project: String) {
    println("Fetch all commits of $project")
    val repo: GHRepository = buildGithubClient().getRepository(project)
    val allCommits: List<Date> = repo.listCommits().toList().map { it.commitDate }.toList()

    val commitsByMonth: MutableMap<String, Int> = allCommits
        .groupingBy {
            dateFormat.format(it)
        }
        .eachCount()
        .toMutableMap()

    generateDateRange(allCommits.min())
        .associate { dateFormat.format(it) to 0 }
        .forEach { (date, count) -> commitsByMonth.putIfAbsent(date, count) }

    val sortedCommitsByMonth: SortedMap<String, Int> = commitsByMonth.toSortedMap()

    val name: String = project.substringAfter("/")
    val targetPath = Path.of("$name-commits.csv")
    targetPath.writeLines(
        sortedCommitsByMonth.map { (date, count) -> "$date,$count" }
    )
}

fun storeReleasesStats(project: String) {
    println("Fetch all releases of $project")
    val repo: GHRepository = buildGithubClient().getRepository(project)
    val allReleasesDates = repo.listReleases().map { it.createdAt }.toList()

    val releasesByMonth: MutableMap<String, Int> = allReleasesDates
        .groupingBy {
            dateFormat.format(it)
        }
        .eachCount()
        .toMutableMap()

    generateDateRange(allReleasesDates.min())
        .associate { dateFormat.format(it) to 0 }
        .forEach { (date, count) -> releasesByMonth.putIfAbsent(date, count) }

    val sortedReleasesByDate: SortedMap<String, Int> = releasesByMonth.toSortedMap()

    val name: String = project.substringAfter("/")
    val targetPath = Path.of("$name-releases.csv")
    targetPath.writeLines(
        listOf(
            sortedReleasesByDate.keys.joinToString(","),
            sortedReleasesByDate.values.joinToString(",")
        )
    )
}

private fun buildGithubClient(): GitHub {
    return GitHubBuilder().withOAuthToken(githubToken).build()
}

fun generateDateRange(startDate: Date): List<Date> {
    val dateRange: MutableList<Date> = ArrayList<Date>()
    val calendar = Calendar.getInstance()
    calendar.time = startDate
    val today = Date()
    while (calendar.time < today) {
        dateRange.add(calendar.time)
        calendar.add(Calendar.MONTH, 1)
    }
    return dateRange
}
