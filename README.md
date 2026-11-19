# AccessGraph

**Accessibility-aware TTC route planning — CSC207, University of Toronto, Fall 2026**

AccessGraph is a planned Java desktop application that helps TTC passengers find routes that meet their mobility needs. It models transit connections as a graph, excludes connections that violate a passenger's accessibility requirements, and recalculates routes when a disruption makes a connection unavailable.

**Team:** Hayk Danghyan, Dean Wu, Hongbo Wang, and Adrian Lau — Group 17.

**Project status:** Runnable Java 11 / Swing prototype with a map-first home page, destination-address entry, permission-based device location, a TTC station directory, and session-only journey management. The embedded map uses OpenStreetMap tiles and a dated official TTC station catalogue. Graph routing, route accessibility evaluation, rerouting, and persistent storage are not implemented yet. Actual device location depends on your browser and OS permissions. TTC realtime integration remains planned. The specification below describes the eventual course MVP.

## Run the interface prototype

Requirements: a JDK **11 or newer**, Maven **3.6.3 or newer**, and a graphical desktop. The app requires no API key or billing account. An internet connection is needed to load map tiles and look up a destination. Maven downloads build plugins and dependencies on the first build and packages the JSON library in the runnable JAR.

```sh
mvn clean package
java -jar target/accessgraph.jar
```

In IntelliJ IDEA, import `pom.xml`, select a JDK 11+ for the project, and run `org.accessgraph.app.Main`. The window opens on Swing's event dispatch thread. A headless environment can run the tests but cannot open the desktop window.

```sh
mvn test
java -jar target/accessgraph.jar --help
```

### What works now

| Screen | Prototype behaviour | Still to implement |
| --- | --- | --- |
| Plan journey (home) | OpenStreetMap basemap with TTC stations, typed destination addresses, and a permission-based device-position pin. Open transit directions in Apple Maps. The inner Stations tab provides the searchable directory. | AccessGraph routing, address-match verification, entrance-level navigation, and live elevator status. |
| Station pairs | Select sample stations, swap endpoints, validate inputs, review a journey request, and name/save a station pair. | Graph search, constraint evaluation, station sequences, travel times, and real results. |
| Accessibility preferences | Edit wheelchair requirements, stair/escalator permissions, and a transfer limit from 0–5; apply or restore defaults. Wheelchair mode excludes stairs and escalators. | Saving/restoring a profile from local storage and enforcing it during routing. |
| Disruption simulation | Mark either of two fictional connections unavailable and restore them; update the displayed simulation count. | Actual connection mapping, graph updates, rerouting, and before/after comparison. |
| Saved trips | Name, load, and delete station pairs within the current session. Loading reviews the pair with the current applied profile. | Persistent journeys and a fresh route search when loading. |

The **Station pairs** sample picker contains **Bloor–Yonge, Eglinton, Finch, Osgoode, Queen's Park, St Andrew, St George, and Union**. These are prototype routing inputs, separate from the larger station-map catalogue. The two simulation connections are fictional. The home map loads when first shown, on explicit map actions, and for changed live positions at most once every 30 seconds while sharing. Preferences, trips, exact device positions, destination addresses, and rendered overlays stay in memory.

“Find route” validates the station pair and displays that routing is not connected. It never returns a sample route as a real result. Duration and transfers remain uncalculated, accessibility remains unknown, and route comparison stays disabled. Preferences, saved trips, and simulation settings reset when the application closes.

### Use the map

1. Open **Plan journey**. The TTC map loads automatically and defaults to the 103 stations that TTC lists as wheelchair-accessible.
2. Enter an address such as **100 Queen St W, Toronto**, then select **Show destination on map**. The red **D** pin marks the address match. Check that it is the intended location.
3. Use **Stations** to search the 109-location directory. Select **Show listed stations** to fit the filtered list, or select a station and choose **Focus selected station**. Clear the accessibility filter to see limited and unknown listings in separate colours.
4. **Open transit directions in Apple Maps** uses the typed address and a fresh device position when available. You can choose an origin in Apple Maps. **Open selected in OpenStreetMap** opens the selected station. The app's accessibility preferences apply to AccessGraph's planned routing. [Apple Maps link format](https://developer.apple.com/documentation/mapkit/unified-map-urls)

The app requests only tiles needed for the current view, follows the provider's HTTP caching instructions, and displays OpenStreetMap attribution on the map. Raw tiles and their cache metadata are stored in `~/.accessgraph/tiles`. Tiles identify the displayed area. AccessGraph draws the station and device pins locally from exact coordinates. [OpenStreetMap tile policy](https://operations.osmfoundation.org/policies/tiles/), [OSMF privacy policy](https://osmfoundation.org/wiki/Privacy_Policy)

An explicit destination search sends the address to Photon's public geocoder. The latest successful match is reused in memory. Typing alone sends no search request. Directions opened in Apple Maps send it the address and a fresh origin when available. [Photon](https://github.com/komoot/photon)

Both public services are intended here for light prototype use and have best-effort availability. To point the app to different compatible services, set the JVM properties `accessgraph.tiles.url` (base URL for `/{z}/{x}/{y}.png`) and `accessgraph.geocoder.url` (Photon search endpoint). [OpenStreetMap tile policy](https://operations.osmfoundation.org/policies/tiles/), [Photon public service guidance](https://github.com/komoot/photon#readme)

### Use your live location

1. On **Plan journey → Your journey**, choose **Use my location**. This starts a local permission page in your default browser; it does not guess your location from an IP address.
2. On that page, choose **Start sharing location**, then allow the browser/operating-system location prompt. Keep the page open and return to Swing. If the browser or OS blocks location, the app gives a message and destination entry/browser directions remain available.
3. Swing shows the actual device coordinates, reported accuracy, and observation time. A blue **U** pin is added to the embedded map. Position fixes update as the device reports changes; the page also requests a fresh fix every 30 seconds for stationary devices. Changed map positions refresh at most every 30 seconds, while the text status updates immediately. Unchanged coordinates do not trigger repeated map downloads.
4. Choose **Stop sharing** in Swing or the browser to end the session. Closing the helper page also stops its device watch. Closing AccessGraph shuts down the local receiver; the helper page detects the ended session and stops. Old and late callbacks are ignored, and stopping clears the device position and the map image containing it.

Swing has no built-in browser-style location permission API, so `BrowserLocationGateway` serves the bundled helper only on `127.0.0.1` at an ephemeral port. The helper uses [`watchPosition`](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation/watchPosition), which requires explicit user permission; modern browsers treat [loopback origins as trustworthy contexts](https://developer.mozilla.org/en-US/docs/Web/Security/Defenses/Secure_Contexts). Sessions use a random fragment token, same-origin checks, and bounded validated updates. There is no location-history storage or public network listener. The browser/OS may use its own network location provider, and its reported accuracy may vary.

Fixes older than **60 seconds** are excluded from the map and the transit directions link. The displayed timestamp is the device observation time. Automated tests simulate the device permission flow. Check actual OS and browser location permissions on your computer.

### Station accessibility evidence and coverage

The bundled snapshot was checked on **November 9, 2026** and contains **109 station locations: 103 affirmative listings, 4 limited listings, and 2 unknown/conflicting listings**. Coordinates and station identifiers come from [TTC's merged GTFS dataset](https://open.toronto.ca/dataset/merged-gtfs-ttc-routes-and-schedules/), feed `S1000544` (November 1–December 19, 2026). This is a snapshot, not live station status.

Accessibility comes from each station's official TTC page, whose URL and check date are retained in every catalogue row. The downloaded GTFS marks **all 114 parent station records** with `wheelchair_boarding=1`, including known incomplete/limited stations, so that field is not used as accessibility evidence. Only a TTC “Accessible” station feature or affirmative overview qualifies. Conflicting details remain unknown: College and Lawrence currently have an accessible badge alongside an overview saying “No”, so neither is included by the default filter. Islington, King, Museum, and Old Mill have limited listings. These conservative classifications describe the source records, not an independent accessibility audit.

Station pages are paired to GTFS parents through their boarding stop codes. Five unmatched retired Line 3 locations (Ellesmere, Lawrence East, McCowan, Midland, and Scarborough Centre) are omitted. The TTC GTFS coordinates identify station locations. The TTC station details pages provide entrance information where available. Elevator outages, partial platform access, construction, and other disability-specific requirements can change whether a journey is usable. Use **View TTC station details** and **Check TTC elevator alerts** for current information.

To regenerate the snapshot, use Python **3.9+** (standard library only), then rebuild:

```sh
python3 scripts/update_station_catalogue.py
python3 -m unittest discover -s scripts -p 'test_*.py'
mvn clean package
```

The updater downloads the official GTFS archive (currently about 68 MB), the TTC station directory, and matching station pages. Failed downloads or an unexpectedly small catalogue abort before replacing the shipped data. Optional `--gtfs-zip`, `--directory-html`, and `--cache-dir` arguments support repeatable local inspection; cached pages retain their original check dates. Resource files are in `src/main/resources/data/`. Coordinate data is attributed to the City of Toronto / TTC under the [Open Government Licence – Toronto](https://open.toronto.ca/open-data-license/).

### Code organization

```text
src/main/java/org/accessgraph/
  app/                 Entry point and constructor-based composition root
  entity/              AccessibilityProfile, Station, and timestamped GeoPosition value objects
  interface_adapter/   PrototypeController and observable session presentation state
  use_case/station_map/ Map/location gateway boundaries and map display request data
  data_access/         Station reader, OpenStreetMap renderer, tile cache, local permission bridge, and browser adapter
  view/                Swing window, five screen panels, and shared styling
src/main/resources/data/
  ttc-stations.tsv     Dated station locations, status, source URLs, and check dates
src/main/resources/location/
  index.html          Local permission page, with separate JS/CSS and no external scripts
src/test/java/org/accessgraph/
  interface_adapter/   Validation, session lifecycle, and request invalidation tests
  data_access/         Catalogue integrity and local HTTP map-adapter tests
  view/                Headless Swing workflows, async map results, errors, and cancellation
scripts/               Catalogue updater, parser checks, and simulated browser-location checks
```

The current controller coordinates presentation actions only. It does not implement the four final journey use cases. The map adapter uses Java 11's HTTP client behind `MapImageGateway`. It renders tiles and overlays from the stations, optional device fix, and destination address in `MapRequest`. `LocationGateway` isolates the local HTTP/browser provider from Swing. Concrete adapters are injected in `AppFactory`, and the window closes location resources on exit. When routing development starts, introduce Swing-independent interactors with input/output boundaries and graph entities. Keep routing, JSON storage, and API response formats out of the views and domain entities.

## Project specification

### Purpose and intended users

The application is intended for TTC passengers who need step-free or wheelchair-accessible journeys, or who want to limit transfers and walking. A route should be recommended only when the available data supports its compatibility with the selected requirements.

The first version will cover a manageable, explicitly documented subset of the TTC network. It will focus on station-to-station travel and relevant entrance, platform, and transfer connections where supporting data is available.

### Core behaviour

- Let passengers select an origin, destination, and accessibility profile through a Swing interface.
- Represent travel and transfer connections using a weighted graph with nonnegative estimated travel times.
- Use Dijkstra's algorithm to find a feasible route, tracking any additional state needed to enforce constraints such as a transfer limit.
- Treat accessibility restrictions as hard constraints: a connection requiring stairs must be excluded when stairs are disallowed.
- Distinguish confirmed accessibility from unknown accessibility. Do not silently treat missing information as accessible.
- Display the route's station sequence, estimated duration, transfer count, and relevant accessibility explanations.
- Apply supported disruptions to the affected connections, then recalculate the selected journey.
- Save accessibility preferences and frequent journeys locally so they survive an application restart.

Travel times may initially be estimates rather than live arrival predictions. Results must identify their data source and whether live, cached, or simulated conditions are being used. The application is a course prototype; it does not provide verified end-to-end navigation guarantees.

## User stories and acceptance criteria

Each teammate will lead at least one complete use case across its application layers. Owners are **to be agreed by the team**; the order below does not assign work to particular members.

### 1. Accessible route planning

> As a TTC passenger, I want to find a route between two stations that meets my accessibility requirements so that I can avoid barriers I cannot navigate.

**Owner:** To be assigned.

Acceptance criteria:

- A passenger can select two supported stations and search using their current accessibility profile.
- Every returned route satisfies the supported hard constraints and uses only available connections.
- Results show the station sequence, estimated duration, transfer count, and accessibility information.
- Invalid selections, unsupported stations, and searches with no feasible route produce clear messages.

### 2. Accessibility profile management

> As a passenger with mobility restrictions, I want to save my accessibility preferences so that future searches automatically consider my needs.

**Owner:** To be assigned.

Acceptance criteria:

- A passenger can configure wheelchair-accessibility requirements, whether stairs and escalators are permitted, and a maximum transfer count.
- The profile is saved locally and restored after restarting the application.
- Subsequent searches use the saved preferences unless the passenger changes them.
- Missing or invalid saved data is reported clearly and does not crash the application; the passenger can confirm a new profile before searching.

### 3. Disruption-aware rerouting

> As a TTC passenger, I want my route to be recalculated when a required connection becomes inaccessible so that I can find a feasible alternative.

**Owner:** To be assigned.

Acceptance criteria:

- A passenger can simulate an outage or closure on a supported connection and restore it afterwards.
- The routing graph reflects the disruption, and recalculation excludes the unavailable connection.
- The application explains why the previous route became invalid and displays a replacement or reports that none exists.
- Live disruption retrieval will be added if feed access and connection mapping are validated. Simulation remains available for a repeatable offline demonstration.

### 4. Saved trips and route comparison

> As a frequent TTC passenger, I want to save common journeys and compare their routes under different conditions so that I can quickly choose a suitable journey.

**Owner:** To be assigned.

Acceptance criteria:

- A passenger can name, save, load, and delete an origin–destination pair.
- Saved trips remain available after restarting the application.
- Loading a trip runs a new search using the current accessibility profile and network conditions rather than assuming an old route remains valid.
- When a disruption changes a route, the passenger can compare the previous and recalculated station sequences, estimated durations, and transfer counts. The previous route is clearly labelled if it is no longer feasible.

## Data sources and external API plan

The following sources support either the station map or planned routing integrations:

| Source | Intended use | Validation needed |
| --- | --- | --- |
| [Toronto Open Data: TTC GTFS](https://open.toronto.ca/dataset/merged-gtfs-ttc-routes-and-schedules/) | Station-map coordinates and identifiers; later, travel connections. | Inspected feed `S1000544`; parent accessibility flags are unsuitable on their own. Routing graph still to implement. |
| [OpenStreetMap raster tiles](https://operations.osmfoundation.org/policies/tiles/) | Keyless basemap with locally rendered station pins. | Live TTC overview verified. Local fixtures cover cache reuse, revalidation, and marker rendering. |
| [Photon](https://github.com/komoot/photon) | Keyless forward geocoding for destination pins. | Toronto City Hall lookup verified live. Local fixtures cover missing matches and malformed responses. |
| Official TTC station pages | Station-level wheelchair-accessibility evidence and source links. | Dated snapshot checked November 9, 2026; two contradictory records remain unknown. No live elevator assessment. |
| [Toronto Open Data: TTC GTFS-Realtime](https://open.toronto.ca/dataset/ttc-gtfs-realtime-gtfs-rt) | Candidate external API for service and accessibility disruptions. | Verify endpoint access from Java, authentication, format, alert coverage, and mapping to specific connections. |
| [TTC elevators and escalators](https://www.ttc.ca/accessibility/Easier-access-on-the-TTC/Elevators-and-Escalators) | Reference for accessibility information and elevator locations or status. | Determine what information can be reliably incorporated and attributed. |
| [GTFS accessibility documentation](https://gtfs.org/getting-started/features/accessibility/) | Interpret accessibility-related fields. | Check which documented fields the selected TTC data actually supplies. |

The MVP must include at least one verified, working external API integration. Downloaded static files and offline HTTP fixtures alone do not complete that requirement. Launch the app online and confirm the TTC overview and a destination pin render from the live services. The proposed TTC realtime source still requires validation for disruption data.

An elevator outage must affect only the connections supported by the data, rather than automatically disabling an entire station. Ambiguous alerts must be identified as such. Deterministic fixtures and clearly labelled simulated conditions will support testing and demonstrations when a live feed is unavailable.

## Technical design

- **Platform:** Java 11 and Swing, built with Maven.
- **Architecture:** Clean Architecture and SOLID principles, with constructor-based dependency injection.
- **Domain:** Stations and network nodes, transit connections, routes, accessibility profiles, disruptions, and saved trips.
- **Use cases:** Interactors with input/output boundaries; controllers, presenters, and view models connect them to Swing.
- **Data access:** Gateway interfaces separate network retrieval and persistence from application logic. Concrete adapters depend on those interfaces.
- **Persistence:** Local JSON files are the initial proposal for profiles and saved trips; the implementation and library choice remain to be agreed.
- **Testing:** JUnit tests for routing, constraints, interactors, disruption updates, and persistence behaviour. Domain logic must be testable without Swing or live network requests.

Routing and use-case logic must not depend on Swing, API response formats, or storage libraries. The exact classes and package structure will be refined during implementation.

## Scope

### Minimum viable product

- A functional Swing route planner for a documented subset of the TTC network.
- Constraint-aware graph routing and clear handling of unavailable routes.
- Persistent profiles and saved journeys.
- Connection-level disruption simulation, rerouting, and before/after comparison.
- One verified, working external API integration.
- Four complete use cases with meaningful contributions from all four teammates.
- Unit tests, design documentation, and a repeatable demonstration.

### Possible extensions

Full-network coverage, live elevator outage integration if not feasible for the MVP, multiple alternative paths for the same conditions, schedule-aware arrival predictions, precise walking distances, a schematic network visualization, and disruption impact analysis.

### Out of scope for the initial version

Web or mobile clients, user accounts, payments, a complex backend, machine learning, and street-level geographic navigation.

## Collaboration

Work will proceed through issues, feature branches, implementation and tests, pull requests, code review, and merges. Each teammate should lead a complete use case and contribute meaningful code, while reviewing and integrating work with the rest of the team. Shared expectations are recorded in [TeamContract.md](TeamContract.md).

## Running the application and demonstrations

Build and launch using the commands above. A repeatable offline interface demonstration is:

1. Open **Station pairs** and select **Find route** with no stations to see validation. Choose **Union** and **Bloor–Yonge**, swap them, and select **Find route** again to see the unconnected search state.
2. Open **Settings**, change the transfer limit, and select **Apply preferences**. Return to **Station pairs** to see the applied summary and invalidated request.
3. Give the station pair a name and select **Save station pair**. In **Saved trips**, select the entry and load it. The planner reviews the pair with the current profile and still reports that routing is not connected.
4. In **Disruption simulation**, mark a sample connection unavailable. The planner's simulation count changes; no route is recalculated. Restore the sample connections afterwards.
5. Delete the saved trip or close and reopen the app to observe that session data is cleared.

For the main page, enter **100 Queen St W, Toronto** under **Plan journey → Your journey**, then show the destination or open transit directions in Apple Maps. Choose **Use my location** and follow the browser permission step to see a device position. For the station directory, open the inner **Stations** tab, search **Union**, and open OpenStreetMap or TTC details. Search **Old Mill** to see that the default filter excludes it; turn the filter off to inspect its limited listing.

JUnit tests cover these interactions without opening a desktop window or contacting external services. Map/location transport tests use loopback HTTP servers; Swing tests use simulated map, device, and browser gateways. Python tests check station-page classification, and Node's built-in runner tests the browser's start/deny/stop/heartbeat flows with a simulated device:

```sh
mvn test
python3 -m unittest discover -s scripts -p 'test_*.py'
node --test scripts/test_location_bridge.cjs
```

Node is only needed for the JavaScript tests; it is not an application runtime dependency. Actual location permissions depend on the browser and operating system. Completed routing and realtime elevator demonstrations remain future work.

## Course references

- [CSC207 project overview](https://q.utoronto.ca/courses/444184/pages/project-overview)
- [CSC207 project expectations](https://q.utoronto.ca/courses/444184/pages/project-expectations)
- [Developing project user stories](https://q.utoronto.ca/courses/444184/pages/developing-project-user-stories)

This specification should be updated as the team confirms ownership, validates data sources, and completes functionality.
