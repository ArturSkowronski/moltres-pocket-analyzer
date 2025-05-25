# Moltres - Pocket Analyzer 🔥

A Kotlin-based tool for analyzing and migrating your Pocket data before the service shuts down. Named after the legendary Fire-type Pokémon, because Pockemon = Pocket Monsters, and this tool helps rescue your digital archive from the flames.

## Background

Recently, news broke that Pocket (formerly Read It Later) is shutting down. For years, Pocket served as a central hub for saving links, articles, and content to read later. While it became increasingly slow and frustrating over time, it still housed years of valuable bookmarks and reading lists.

With Pocket's closure, this tool provides a way to analyze your exported data and prepare it for migration to alternative services.

## Technologies Explored

This project serves as a playground for several technologies highlighted at KotlinConf:

- **Kotlin Scripts (.kts)** - Executable Kotlin scripts for data processing
- **DuckDB** - High-performance analytical database (planned)
- **Kotlin Notebook** - Interactive data analysis (planned)
- **Kotlin LSP** - Language Server Protocol integration

## Features

- **Deduplication**: Removes duplicate URLs based on normalized URLs
- **Data Cleaning**: Handles missing titles, invalid URLs, and malformed data
- **Export Ready**: Outputs clean CSV format suitable for importing into other services

## Usage

### Prerequisites

- Kotlin runtime environment
- Your Pocket export file (CSV format)

### Running the Analyzer

```bash
# Make the script executable
chmod +x scripts/PocketAnalyzer.main.kts

# Analyze CSV export  
./scripts/PocketAnalyzer.main.kts part_000000.csv
```

### Output

The analyzer generates a cleaned CSV file (`cleaned_links.csv`) with the following columns:
- `title` - Article/page title
- `url` - Cleaned and normalized URL
- `added_iso` - Date added to Pocket
- `alive` - URL status (currently always true)
- `tags` - Pipe-separated list of tags

## Data Processing Pipeline

1. **Parsing**: Extracts title, URL, timestamp, and tags from raw data
2. **Normalization**: Standardizes URLs (HTTPS, removes trailing slashes)
3. **Deduplication**: Removes duplicate entries based on normalized URLs
4. **Export**: Generates clean CSV output for migration

## Future Enhancements
- [ ] **DuckDB Integration**: High-performance analytics on large datasets
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
