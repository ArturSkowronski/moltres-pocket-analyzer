# Moltres - Pocket Analyzer 🔥

A Kotlin-based tool for analyzing and migrating your Pocket data before the service shuts down. Named after the legendary Fire-type Pokémon, because Pockemon = Pocket Monsters, and this tool helps rescue your digital archive from the flames.

## Background

Recently, news broke that Pocket (formerly Read It Later) is shutting down. For years, Pocket served as a central hub for saving links, articles, and content to read later. While it became increasingly slow and frustrating over time, it still housed years of valuable bookmarks and reading lists.

With Pocket's closure, this tool provides a way to analyze your exported data and prepare it for migration to alternative services.

## Technologies Explored

This project serves as a playground for several technologies highlighted at KotlinConf:

- **Kotlin Scripts (.kts)** - Executable Kotlin scripts for data processing
- **DuckDB** - High-performance analytical database
- **Kotlin Notebook** - Interactive data analysis (supported, see below)
- **Kotlin LSP** - Language Server Protocol integration

## Features

- **Deduplication**: Removes duplicate URLs based on normalized URLs
- **Data Cleaning**: Handles missing titles, invalid URLs, and malformed data
- **Export Ready**: Outputs clean CSV format suitable for importing into other services
- **DuckDB Export**: Optionally saves deduplicated data to a DuckDB database for scalable querying
- **Kotlin Notebook Support**: Explore, analyze, and visualize your Pocket data interactively

## Usage

### Prerequisites

- Kotlin runtime environment
- Your Pocket export file (CSV format)
- Java (for DuckDB JDBC)
- (Optional) [Kotlin Jupyter Notebook](https://github.com/Kotlin/kotlin-jupyter) for interactive analysis

### Running the Analyzer

```bash
# Make the script executable
chmod +x scripts/PocketAnalyzer.main.kts

# Analyze a single CSV export
./scripts/PocketAnalyzer.main.kts export_data/part_000000.csv

# Analyze and merge all CSVs in a directory
./scripts/PocketAnalyzer.main.kts export_data/

# Save deduplicated data to DuckDB (in addition to CSV)
./scripts/PocketAnalyzer.main.kts export_data/ --duckdb-path=output/pocket.duckdb
```

> **Note:** You must create the DuckDB database file (e.g. `output/pocket.duckdb`) before running the script with `--duckdb-path`. You can do this by running `duckdb output/pocket.duckdb` in your terminal, which will create the file if it does not exist, then exit DuckDB with `.quit`.

### Kotlin Notebook Support

You can use this project in a [Kotlin Jupyter Notebook](https://github.com/Kotlin/kotlin-jupyter) for interactive data exploration and visualization:

1. Install Kotlin Jupyter (see [official instructions](https://github.com/Kotlin/kotlin-jupyter)).
2. Create a new `.ipynb` or `.kts` notebook in the project directory.
3. Add the following to a cell to import the main logic:
   ```kotlin
   %useLatestDescriptors
   %classpath add scripts/PocketAnalyzer.main.kts
   ```
4. You can now use the functions and data classes from the script to load, analyze, and visualize your Pocket data interactively.

**Example notebook cell:**
```kotlin
val items = parseCsv("export_data/part_000000.csv")
items.take(5)
```

You can also use libraries like Krangl, Lets-Plot, or Kotlin DataFrame for further analysis and visualization.

### Output

- The analyzer generates a cleaned CSV file (`cleaned_links.csv`) in the `output/` directory.
- If `--duckdb-path` is provided, deduplicated data is also saved to a DuckDB database at the specified path. Each URL is a primary key, and the value is stored in a table `pocket_items`.

#### Example DuckDB Usage

You can use any DuckDB-compatible tool or library to query the resulting database. Each row contains the URL, title, added date, and tags as columns.

## Data Processing Pipeline

1. **Parsing**: Extracts title, URL, timestamp, and tags from raw data
2. **Normalization**: Standardizes URLs (HTTPS, removes trailing slashes)
3. **Deduplication**: Removes duplicate entries based on normalized URLs
4. **Export**: Generates clean CSV output for migration and/or saves to DuckDB

## Project Setup

1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd moltres-pocket-analyzer
   ```
2. Ensure you have Java and Kotlin installed.
3. (Optional) For DuckDB support, no extra steps are needed; the script will download the dependency automatically.
4. Place your Pocket CSV exports in a directory (e.g., `export_data/`).
5. Run the script as shown above.

## Future Enhancements
- [ ] **Kotlin Notebook**: Interactive data exploration and visualization
- [ ] **URL Validation**: Check if saved links are still accessible
- [ ] **Migration Helpers**: Direct export to popular alternatives (Instapaper, Raindrop, etc.)
- [ ] **Tag Analysis**: Insights into your reading patterns and interests

## Why "Moltres"?

Pokémon is short for "Pocket Monsters", and Moltres is the legendary Fire-type Pokémon. As Pocket burns down, Moltres rises from the ashes to help save your digital archive.

## Contributing

This project serves as both a practical tool and a learning playground for modern Kotlin development. Contributions exploring the planned technologies (DuckDB, Kotlin Notebook, etc.) are especially welcome.

## License

This project is licensed under the terms specified in the LICENSE file.

---

*"Gotta catch 'em all... before the service shuts down!"* 🔥📱
