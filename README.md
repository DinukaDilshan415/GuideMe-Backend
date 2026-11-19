# GuideMe – Backend

The REST API behind the **[GuideMe Android app](https://github.com/DinukaDilshan415/GuideMe-Android)**, a travel and tourism platform. Travellers use the app to find and book hotels, services and things to do, and local businesses use it as partners to list what they offer. This server handles users, partners, listings, availability checks, bookings and reviews, and stores everything in MySQL.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-Jersey%203.1-blue)
![Tomcat](https://img.shields.io/badge/Tomcat-10.1%20(embedded)-F8DC75?logo=apachetomcat&logoColor=black)
![Hibernate](https://img.shields.io/badge/Hibernate-6.1-59666C?logo=hibernate)
![MySQL](https://img.shields.io/badge/MySQL-8%2B-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven)

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [Getting Started](#getting-started)
- [Exposing the API to the App](#exposing-the-api-to-the-app)
- [API Reference](#api-reference)
- [Author](#author)

---

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Server | Embedded Apache Tomcat 10.1, started from `Main.java` |
| REST framework | Jersey 3.1 (JAX-RS / Jakarta EE), Jackson for JSON |
| Persistence | Hibernate ORM 6.1 with MySQL Connector/J 9 |
| Utilities | Gson, Lombok |
| Build | Maven (`war` packaging) |

---

## Architecture

```
 GuideMe Android app
        │  HTTP / JSON (Retrofit)
        ▼
┌───────────────────────────────────────────────┐
│  Embedded Tomcat  :8080   context: /guide_me  │
│  ┌─────────────────────────────────────────┐  │
│  │ Jersey ServletContainer  (AppConfig)    │  │
│  │   controller/  → JAX-RS resources       │  │
│  │   service/     → business logic         │  │
│  │   entity/      → Hibernate entities     │  │
│  └─────────────────────────────────────────┘  │
└───────────────────────┬───────────────────────┘
                        ▼
                MySQL  (guideme_db)
```

Each request flows **Controller → Service → Hibernate Session → MySQL**. Controllers only map HTTP routes. The service classes open Hibernate sessions through `HibernateUtil` and build the JSON responses.

---

## Project Structure

```
src/main/
├── java/me/dinuka/guideme/
│   ├── Main.java          # Starts embedded Tomcat on port 8080 with context /guide_me
│   ├── config/
│   │   └── AppConfig.java # Jersey ResourceConfig (scans the controller package)
│   ├── controller/        # REST resources: User, Partner, Listing, Hotel, Service,
│   │                      #                 ThingsToDo, Booking, Facility, Test
│   ├── service/           # Business logic and Hibernate queries
│   ├── dto/               # Request/response objects
│   ├── entity/            # JPA/Hibernate entities (tables)
│   └── util/
│       └── HibernateUtil.java   # SessionFactory singleton
├── resources/
│   └── hibernate.cfg.xml  # Database connection and Hibernate settings
└── webapp/
    └── WEB-INF/
        ├── web.xml        # Maps the Jersey servlet to /*
        └── views/         # Default index page
```

---

## Data Model

The main entities in `entity/`:

| Group | Entities |
|---|---|
| **Users** | `User`, `UserAddress` |
| **Partners** | `Partner`, `PartnerAddress`, `PartnerTypes`, `PartnerPlanUpgrade`, `Qualifications` |
| **Listings** | `Listing`, `ListingType`, `ListingImages`, `Reviews`, `Status` |
| **Hotels** | `HotelListing`, `RoomTypes`, `Facility`, `HotelListingHasFacilities` |
| **Services and activities** | `ServiceListing`, `ThingsToDoListing` |
| **Bookings** | `Booking`, `BookingItemHotel`, `BookingItemService`, `BookingItemThingsToDo` |

A `Listing` holds the shared details: title, description, address, price, location and images. It's then extended by a hotel, service or things-to-do listing. A `Booking` has one booking-item row for the type of listing that was booked.

---

## Getting Started

### Prerequisites
- **JDK 17**
- **Maven 3.8+**
- **MySQL 8+**
- An IDE such as IntelliJ IDEA (recommended)

### 1. Clone

```bash
git clone https://github.com/DinukaDilshan415/GuideMe-Backend.git
cd GuideMe-Backend
```

### 2. Create the database

```sql
CREATE DATABASE guideme_db;
```

`hibernate.hbm2ddl.auto` is set to `update`, so Hibernate creates and updates the tables when the server starts.

> **Seed data:** lookup tables such as `listing_type`, `facility`, `status` and `partner_types` must contain rows before the app can create listings, bookings or partners.

### 3. Configure the connection

Edit `src/main/resources/hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/guideme_db</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">your_password</property>
```

### 4. Run

Run **`me.dinuka.guideme.Main`** from your IDE. Alternatively, build and run it from the command line:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="me.dinuka.guideme.Main"
```

> `exec:java` needs the `exec-maven-plugin`. Maven usually resolves it automatically. If it doesn't, run `Main` from the IDE.

Run it from the project root, because Tomcat looks for `src/main/webapp` relative to the working directory. The API is then available at:

```
http://localhost:8080/guide_me/
```

Quick check:

```bash
curl http://localhost:8080/guide_me/test
```

---

## Exposing the API to the App

The Android app reads its base URL from `client/RetrofitClient.java` in the app project.

- **Android emulator** on the same machine: use `http://10.0.2.2:8080`
- **Physical device**: use your computer's local network IP, or a tunnel:
  ```bash
  ngrok http 8080
  ```
  Then put the ngrok HTTPS address into `RetrofitClient.java`. The app adds `/guide_me/` itself.

---

## API Reference

Base URL: `http://<host>:8080/guide_me`. Requests and responses are JSON.

### User — `/user`
| Method | Path | Description |
|---|---|---|
| POST | `/user/register` | Register a traveller |
| GET | `/user/{email}` | Get a user by email |
| GET | `/user/address/{email}` | Get a user's address |
| POST | `/user/saveorupdateaddress` | Create or update an address |
| PUT | `/user/updatemobile` | Update mobile number |
| PUT | `/user/updatenameandimage` | Update name and profile image |
| PUT | `/user/updatecountry` | Update country |

### Partner — `/partner`
| Method | Path | Description |
|---|---|---|
| POST | `/partner/register` | Register a partner (business) |
| GET | `/partner/{email}` | Get a partner by email |
| GET | `/partner/address/{email}` | Get a partner's address |
| GET | `/partner/checkpartnership/{email}` | Check partnership/plan status |
| POST | `/partner/upgradeplan` | Upgrade the partnership plan |
| POST | `/partner/cancelplan` | Cancel the partnership plan |
| POST | `/partner/saveorupdateaddress` | Create or update an address |
| PUT | `/partner/updatemobile` | Update mobile number |
| PUT | `/partner/updatenameandimage` | Update name and image |
| PUT | `/partner/updatecountry` | Update country |

### Listing — `/listing`
| Method | Path | Description |
|---|---|---|
| GET | `/listing/types/getall` | All listing types (Hotel, Service, Things To Do) |
| GET | `/listing/{email}` | A partner's listings |
| POST | `/listing/addnewlisting` | Create a new listing |
| GET | `/listing/reviews/{lid}` | Reviews for a listing |
| POST | `/listing/addreview` | Add a review |

### Hotels, Services, Things To Do
| Method | Path | Description |
|---|---|---|
| GET | `/hotels/{key}` | List hotels |
| POST | `/hotels/checkavailability` | Check room availability for dates |
| GET | `/service/{key}` | List services |
| POST | `/service/checkavailability` | Check service availability |
| GET | `/thingstodo/{key}` | List things to do |
| POST | `/thingstodo/checkavailability` | Check activity availability |

### Booking — `/booking`
| Method | Path | Description |
|---|---|---|
| GET | `/booking/{email}` | A user's bookings |
| GET | `/booking/listingitem/{id}` | Bookings for a listing item (for partners) |
| POST | `/booking/addnewbooking` | Create a booking |

---

## Related

- 📱 **Android app:** [GuideMe-Android](https://github.com/DinukaDilshan415/GuideMe-Android)

## Author

**Dinuka Dilshan**
- GitHub: [@DinukaDilshan415](https://github.com/DinukaDilshan415)
