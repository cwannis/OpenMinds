# OpenMinds - Donation Management App

Docker setup for the PHP API and MariaDB backend.

## Start

```bash
docker compose up -d --build
```

or with helper scripts:

- Linux/macOS: `./run-local.sh` (foreground, `Ctrl+C` to stop)
- PowerShell: `./run-local.ps1` (foreground, `Ctrl+C` to stop)
- Windows cmd: `run-local.bat` (foreground, `Ctrl+C` to stop)

API base URL from your host machine:

`http://localhost:8080/openMinds/phpFile/`

API base URL from Android emulator:

`http://10.0.2.2:8080/openMinds/phpFile/`

## Stop

```bash
docker compose down
```

or:

- `./run-local.sh stop`
- `./run-local.ps1 stop`
- `run-local.bat stop`

## Reset database (destroys all data and reinitializes)

```bash
docker compose down -v
docker compose up -d --build
```

or:

- `./run-local.sh reset`
- `./run-local.ps1 reset`
- `run-local.bat reset`

## Notes

- DB and API credentials are defined in `docker-compose.yml` via environment variables.
- `phpFile/bdd.php` reads DB credentials from env vars.
- `phpFile/Api.php` reads API key from env vars.
- Initial tables and mock data are created by `docker/mysql/init/01-schema.sql`.

## Default Accounts

### Admin
- Email: `admin@openminds.local`
- Password: `admin`
- Role: Admin for "France Assos Santé" association

### Regular Users
- Email: `jean.martin@email.com` / Password: `test123`
- Email: `sophie.bernard@email.com` / Password: `test123`
- Email: `pierre.leroy@email.com` / Password: `test123`

## API Endpoints

### Authentication
- `POST getUserData.php?email=X&password=Y` - Login
- `POST createUser.php?name=X&mail=Y&password=Z&organization=W` - Register
- `POST changePassword.php?email=X&newpsw=Y` - Change password
- `POST mailExist.php?email=X` - Check if email exists
- `POST mailRecup.php?code=X&mailTo=Y` - Send recovery code

### Associations
- `POST getAllAssociations.php` - List all active associations
- `POST getAllAssociations.php?category=X` - Filter by category
- `POST getAllAssociations.php?search=X` - Search by name/description
- `POST getCategories.php` - List all categories

### Donations
- `POST createDonation.php?association_id=X&amount=Y` - One-time donation
- `POST createDonation.php?association_id=X&amount=Y&user_id=Z` - Authenticated donation
- `POST createDonation.php?association_id=X&amount=Y&guest_email=Z` - Guest donation
- `POST createRecurringDonation.php?user_id=X&association_id=Y&amount=Z&frequency=monthly|annual` - Create recurring
- `POST getUserDonations.php?user_id=X` - Get user's donation history
- `POST getUserRecurringDonations.php?user_id=X` - Get user's recurring donations
- `POST cancelRecurringDonation.php?user_id=X&recurring_id=Y` - Cancel recurring donation

### Admin
- `POST getAdminStats.php?user_id=X` - Get admin stats for user's association
- `POST getAdminStats.php?user_id=X&year=Y` - Filter by year

### Badges
- `POST getBadgesOfUser.php?id=X` - Get user's badges
