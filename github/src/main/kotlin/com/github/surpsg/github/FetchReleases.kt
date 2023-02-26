package com.github.surpsg.github

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
    }
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
