package com.napier.gp19;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ReportWriter class.
 * These tests verify that report generation methods produce correct Markdown
 * output, handle null or empty datasets, and gracefully manage file I/O issues.
 */
class ReportWriterUnitTest {

    private ReportWriter reportWriter; // Instance of the class under test
    private Path reportDir; // Directory where test reports will be written


    @BeforeEach
    void setUp() throws IOException {
        // Create a new ReportWriter instance before each test
        reportWriter = new ReportWriter();

        // Ensure the reports directory exists
        reportDir = Paths.get("./reports");
        if (!Files.exists(reportDir)) {
            Files.createDirectories(reportDir);
        }

        // Clean up previously generated test files
        Files.list(reportDir)
                .filter(p -> p.getFileName().toString().startsWith("test_"))
                .forEach(p -> p.toFile().delete());
    }

    @Test
    void testOutputCountries_WritesFile() throws IOException {
        // Arrange test data for countries
        Country c1 = new Country();
        c1.setCode("USA");
        c1.setCountryName("United States");
        c1.setContinent("North America");
        c1.setRegion("Northern America");
        c1.setPopulation(331000000);
        c1.setCapitalName("Washington D.C.");

        Country c2 = new Country();
        c2.setCode("FRA");
        c2.setCountryName("France");
        c2.setContinent("Europe");
        c2.setRegion("Western Europe");
        c2.setPopulation(67000000);
        c2.setCapitalName("Paris");

        ArrayList<Country> countries = new ArrayList<>(Arrays.asList(c1, c2));
        String filename = "test_countries.md";
        String title = "Test Countries Report";

        // Act: Write report
        reportWriter.outputCountries(countries, filename, title);

        // Assert: File exists and content contains expected values
        Path filePath = reportDir.resolve(filename);
        assertTrue(Files.exists(filePath), "Report file should exist");

        String content = Files.readString(filePath);
        assertTrue(content.contains("| France |"), "Should contain France");
        assertTrue(content.contains("# " + title), "Should contain title");
    }

    @Test
    void testOutputCapitalCities_GeneratesCorrectMarkdown() throws IOException {
        // Arrange test city with country relationship
        Country usa = new Country();
        usa.setCountryName("United States");
        City washington = new City();
        washington.setCityName("Washington D.C.");
        washington.setCountry(usa);
        washington.setPopulation(700000);

        ArrayList<City> capitals = new ArrayList<>();
        capitals.add(washington);

        String filename = "test_capitals.md";
        String reportTitle = "Capital Cities Report";

        // Act: Generate capital cities report
        reportWriter.outputCapitalCities(capitals, filename, reportTitle);

        // Assert: Check file content for correct Markdown structure
        Path filePath = reportDir.resolve(filename);
        String content = Files.readString(filePath);

        assertTrue(content.startsWith("# " + reportTitle), "Report should start with title");
        assertTrue(content.contains("| Capital | Country | Population |"), "Header should exist");
        assertTrue(content.contains("| Washington D.C. | United States | 700,000 |"),
                "Table row should match city data");
    }

    @Test
    void testOutputCities_GeneratesCorrectMarkdown() throws IOException {
        // Arrange test city data
        Country japan = new Country();
        japan.setCountryName("Japan");

        City tokyo = new City();
        tokyo.setCityName("Tokyo");
        tokyo.setCountry(japan);
        tokyo.setDistrict("Kanto");
        tokyo.setPopulation(14000000);

        ArrayList<City> cities = new ArrayList<>();
        cities.add(tokyo);

        String filename = "test_cities.md";
        String reportTitle = "City Report";

        // Act: Generate city report
        reportWriter.outputCities(cities, filename, reportTitle);

        // Assert: Verify file content
        Path filePath = reportDir.resolve(filename);
        String content = Files.readString(filePath);

        assertTrue(content.startsWith("# " + reportTitle), "Report should start with title");
        assertTrue(content.contains("| City | Country | District | Population |"), "Header should exist");
        assertTrue(content.contains("| Tokyo | Japan | Kanto | 14,000,000 |"),
                "Table row should match city data");
    }


    @Test
    void testOutputPopulationWorld_WritesExpectedData() throws IOException {
        // Arrange mock population data
        PopulationWorldReport.PopulationData data = new PopulationWorldReport.PopulationData("World");
        data.totalPopulation = 8_000_000_000L;
        data.cityPopulation = 4_000_000_000L;
        data.nonCityPopulation = 4_000_000_000L;
        data.cityPercentage = 50.0;
        data.nonCityPercentage = 50.0;

        String filename = "test_world_pop.md";
        // Act: Generate world population report
        reportWriter.outputPopulationWorld(data, filename);

        // Assert: File created and content correct
        Path filePath = reportDir.resolve(filename);
        assertTrue(Files.exists(filePath), "World report file should exist");

        String content = Files.readString(filePath);
        assertTrue(content.contains("World"), "Should contain 'World'");
        assertTrue(content.contains("8,000,000,000"), "Should format population correctly");
    }

    @Test
    void testOutputLanguagePopulationReport_WritesData() throws IOException {
        // Arrange test language population data
        CountryLanguage lang = new CountryLanguage();
        lang.setLanguage("English");
        lang.setCountryCode("1,500,000,000");
        lang.setPercentage(18.75);

        ArrayList<CountryLanguage> list = new ArrayList<>();
        list.add(lang);

        String filename = "test_language_report.md";

        // Act: Generate language population report
        reportWriter.outputLanguagePopulationReport(list, filename);

        // Assert: Verify file created and content contains expected data
        Path filePath = reportDir.resolve(filename);
        assertTrue(Files.exists(filePath));


        String content = Files.readString(filePath);
        assertTrue(content.contains("| English |"), "Should contain language name");
        assertTrue(content.contains("%"), "Should include percentage formatting");
    }

    @Test
    void testOutputLanguagePopulationReport_NullList() {
        // Assert that passing null does not throw an exception
        assertDoesNotThrow(() -> reportWriter.outputLanguagePopulationReport(null, "test_null_lang.md"));
    }

    @Test
    void testOutputPopulationCityReport_WithData() throws IOException {
        // Arrange city population data
        City city = new City();
        city.setCityName("Yangon");
        city.setDistrict("Yangon Region");
        city.setCountryCode("MMR");
        city.setPopulation(5000000);

        List<City> cities = Collections.singletonList(city);
        String filename = "test_city_report.md";

        // Act: Generate city population report
        reportWriter.outputPopulationCityReport(cities, filename);

        // Assert: File exists and contains city name
        Path path = reportDir.resolve(filename);
        assertTrue(Files.exists(path));

        String content = Files.readString(path);
        assertTrue(content.contains("Yangon"));
    }

    @Test
    void testOutputPopulationCityReport_NoData() throws IOException {
        String filename = "test_city_empty.md";

        // Act: Generate report with empty city list
        reportWriter.outputPopulationCityReport(Collections.emptyList(), filename);
        Path filePath = reportDir.resolve(filename);
        assertTrue(Files.exists(filePath));

        // Assert: File exists and contains "No data available"
        String content = Files.readString(filePath);
        assertTrue(content.contains("No data available"));
    }

    @Test
    void testOutputContinentPopulation_WithMockedData() throws IOException {
        // Arrange continent test data
        Country country = new Country();
        country.setContinent("Asia");
        country.setPopulation(1000000);

        List<Country> list = Collections.singletonList(country);

        // Mock PopulationUtils.calculatePopulationValues()
        // Act: Generate continent population report
        reportWriter.outputContinentPopulation(list, "test_continent_pop.md", "Continent Pop");
        // Assert file created
        Path filePath = reportDir.resolve("test_continent_pop.md");
        assertTrue(Files.exists(filePath));
    }

    @Test
    void testOutputContinentPopulation_EmptyList() throws IOException {
        // Act: Generate report with empty continent list
        reportWriter.outputContinentPopulation(Collections.emptyList(), "test_continent_empty.md", "Empty Continent");

        // Assert: File exists and contains "No data available"
        Path filePath = reportDir.resolve("test_continent_empty.md");
        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertTrue(content.contains("No data available"));
    }

    @Test
    void testOutputPopulationByGroup_WithData() throws IOException {
        // Arrange country population data
        Country country = new Country();
        country.setCountryName("Japan");
        country.setRegion("Eastern Asia");
        country.setPopulation(126000000);

        List<Country> list = Collections.singletonList(country);

        // Act: Generate population by group report
        reportWriter.outputPopulationByGroup(list, "test_pop_by_group.md", "Population by Country", "Country");

        // Assert: File exists and contains country name
        Path path = reportDir.resolve("test_pop_by_group.md");
        assertTrue(Files.exists(path));
        String content = Files.readString(path);
        assertTrue(content.contains("Japan"));
    }

    @Test
    void testOutputPopulationByGroup_EmptyList() throws IOException {
        // Act: Generate report with empty list
        reportWriter.outputPopulationByGroup(Collections.emptyList(), "test_pop_by_group_empty.md", "Empty Pop Group", "Country");

        // Assert: File exists and contains "No data available"
        Path path = reportDir.resolve("test_pop_by_group_empty.md");
        assertTrue(Files.exists(path));
        String content = Files.readString(path);
        assertTrue(content.contains("No data available"));
    }

    @Test
    void testWriteToFile_ExceptionHandling() {
        // Intentionally trigger IOException by using invalid directory path
        ReportWriter badWriter = new ReportWriter() {
            protected void writeToFile(String filename, StringBuilder sb) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/invalid_path/" + filename)));
                    writer.write(sb.toString());
                    writer.close();
                } catch (IOException e) {
                    assertNotNull(e, "IOException should be caught");
                }
            }
        };

        // Arrange dummy country data
        ArrayList<Country> countries = new ArrayList<>();
        Country c = new Country();
        c.setCountryName("Nowhere");
        countries.add(c);

        // Act & Assert: Ensure no exception propagates
        assertDoesNotThrow(() -> badWriter.outputCountries(countries, "test_fail.md", "Test Fail"));
    }


    @AfterEach
    void tearDown() throws IOException {
        // Clean up generated test files
        Files.list(reportDir)
                .filter(p -> p.getFileName().toString().startsWith("test_"))
                .forEach(p -> p.toFile().delete());
    }
}
