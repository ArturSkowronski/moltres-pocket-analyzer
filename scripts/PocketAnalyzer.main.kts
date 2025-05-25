#!/usr/bin/env kotlin
// PocketAnalyzer.main.kts – now handles CSV from Pocket export
// ---------------------------------------------------------------
// Usage:
//   chmod +x PocketAnalyzer.main.kts
//   ./PocketAnalyzer.main.kts <file.csv|directory>
// If you provide a path containing "csv", the script will load CSV; otherwise – JSON.
// ---------------------------------------------------------------
@file:DependsOn("com.opencsv:opencsv:5.9")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
@file:DependsOn("io.ktor:ktor-client-core:2.3.4")

import com.opencsv.CSVReaderBuilder
import java.io.File
import java.io.FileReader
import kotlin.system.exitProcess


data class PocketItem(
    val title: String,
    val url: String,
    val added: String,
    val tags: List<String>
)

// ----------------------- CSV PARSER ---------------------- //
fun parseCsv(path: String): List<PocketItem> {
    val reader = CSVReaderBuilder(FileReader(path)).withSkipLines(1).build() // assuming header
    val rows = reader.readAll().map { it as Array<String> }
    return rows.mapNotNull { row ->
        if (row.size < 4) return@mapNotNull null
        val title = row[0].ifBlank { "<no-title>" }
        val url = row[1]
        val timeAdded = row[2]
        val tagString = if (row.size > 4) row[4] else row[3]
        val tags = if (tagString.isBlank()) emptyList() else tagString.split("|")
        PocketItem(
            title = title,
            url = url,
            added = timeAdded,
            tags = tags
        )
    }
}

// -------------------------- UTILS ------------------------- //
fun normalizeUrl(u: String): String {
    var url = u.trim()
    url = url.replaceFirst("http://", "https://")
    if (url.endsWith('/')) url = url.dropLast(1)
    return url.lowercase()
}

// ------------------------ SCRIPT ------------------------ //
if (args.isEmpty()) {
    println("Usage: PocketAnalyzer.main.kts <file.csv|directory>")
    System.exit(1)
}

val filePath = args[0]
val file = File(filePath)

fun getCsvFilesFromDir(dir: File): List<File> =
    dir.listFiles { f -> f.isFile && f.extension.equals("csv", ignoreCase = true) }?.sortedBy { it.name } ?: emptyList()

val csvFiles: List<File> = when {
    file.isDirectory -> getCsvFilesFromDir(file)
    file.isFile && file.extension.equals("csv", ignoreCase = true) -> listOf(file)
    else -> throw Exception("Unsupported input. Please provide a CSV file or directory containing CSV files.")
}

if (csvFiles.isEmpty()) {
    println("No CSV files found!")
    exitProcess(1)
}

// Read headers from all files and check if they match
val headers = csvFiles.map { csv ->
    FileReader(csv).use { fr ->
        val firstLine = fr.readLines().firstOrNull()?.trim() ?: ""
        firstLine
    }
}

val allHeadersSame = headers.distinct().size == 1
if (!allHeadersSame) {
    println("CSV headers do not match across files:")
    headers.distinct().forEachIndexed { idx, h -> println("Header #$idx: $h") }
    exitProcess(1)
}

val allRows = mutableListOf<PocketItem>()
csvFiles.forEachIndexed { idx, csv ->
    val skipLines = if (idx == 0) 1 else 1 // always skip header
    val reader = CSVReaderBuilder(FileReader(csv)).withSkipLines(skipLines).build()
    val rows = reader.readAll().map { it as Array<String> }
    rows.mapNotNullTo(allRows) { row ->
        if (row.size < 4) return@mapNotNullTo null
        val title = row[0].ifBlank { "<no-title>" }
        val url = row[1]
        val timeAdded = row[2]
        val tagString = if (row.size > 4) row[4] else row[3]
        val tags = if (tagString.isBlank()) emptyList() else tagString.split("|")
        PocketItem(
            title = title,
            url = url,
            added = timeAdded,
            tags = tags
        )
    }
}

val rawItems: List<PocketItem> = allRows

println("Loaded: ${rawItems.size} records…")

if (rawItems.isEmpty()) {
    println("No data to process!")
    exitProcess(1)
}

val deduped = rawItems.distinctBy { normalizeUrl(it.url) }
println("After deduplication: ${deduped.size}")

// Simplified version without HTTP client
val cleaned = deduped.map { it to true }

try {
    File("../output/cleaned_links.csv").printWriter().use { pw ->
        pw.println("title,url,added_iso,alive,tags")
        cleaned.forEach { (item, alive) ->
            val tagsEsc = item.tags.joinToString("|") { it.replace(",", " ") }
            pw.println("\"${item.title.replace("\"", "\"\"")}\",${item.url},${item.added},$alive,$tagsEsc")
        }
    }
    println("Results saved to 'cleaned_links.csv'")
} catch (e: Exception) {
    println("Error saving results: ${e.message}")
    System.exit(1)
}
