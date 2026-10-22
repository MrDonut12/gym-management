# Gym Management System — schema / seed (SRS v1.1, Must)

These two scripts are intended for **SQL Server 2022** in a **fresh local development/test database**. They are generated from the verified 6-table structure in the project's existing `docs/sql_scripts/GymDB_Full_Setup.sql`, with new, deterministic sample data. They **do not replace a migration** for an existing database.

## Run order

1. Open `schema.sql` in SQL Server Management Studio (SSMS) or a SQL client that supports `GO` batch separators. Execute the entire file. It creates `GymDB` if needed and defines the six tables, PK/FK/CHECK/UNIQUE/DEFAULT constraints, filtered indexes and `seq_paid_sequence`.
2. Open `seed.sql` and execute it. It checks the schema exists and every table is empty before writing any sample data.
3. To test creation of a second plan with an existing duration, run `test_duplicate_duration.sql` after seeding. It performs an `INSERT` inside a transaction and then rolls it back.
4. Read the verification query at the bottom of `seed.sql`. Expected rows:

| Table | Expected rows |
|---|---:|
| `accounts` | 5 |
| `members` | 100 |
| `plan_types` | 4 |
| `memberships` | 30 |
| `payment_transactions` | 30 |
| `visits` | 15 |

Payment states: 22 `PAID`, 4 `PENDING`, 4 `CANCELLED`. Membership renewals: members 1 and 2 have consecutive monthly schedules. Visit states: 10 `COMPLETED`, 4 `AUTO_CLOSED`, 1 deliberately overdue `OPEN` to exercise lazy closure.

**Safe behavior:** no `DROP DATABASE`, no `DROP TABLE`. Both scripts stop on detected existing schema/data rather than overwriting it. If `GymDB` already contains the tables created by `GymDB_Full_Setup.sql`, do **not** rerun `schema.sql` over it; use a separate empty test instance/database or prepare an explicit reviewed migration.

**Existing DB migration:** If `GymDB` already exists and includes the old `UQ_plan_types_duration`, run `migration_drop_plan_duration_unique.sql` once after backing up the DB. It only drops the named duration UNIQUE and does not delete any tables or seed data. Also remove the same constraint from the repository’s `docs/sql_scripts/GymDB_Full_Setup.sql` before future fresh deployments; this ZIP does not modify your GitHub repository.

## Integration notes (read before coding DAO)

| DB column | Existing DAO TODO / model difference |
|---|---|
| `members.birth_date` | `MemberDAO` TODO refers to `date_of_birth`; map to `birth_date`. |
| `payment_transactions.method` | `TransactionDAO` TODO refers to `payment_method`; map to `method`. |
| `visits.closed_by_account_id` | `VisitDAO` TODO refers to `closed_by`; map to `closed_by_account_id`. |
| `plan_types.name_normalized` | Insert and update normalized name together with display name. |
| `plan_types.created_by` | `NOT NULL`; T12/T01 must agree on a way to supply the acting Owner without changing frozen public DAO method signatures. |
| `accounts.deleted_at` | On soft-delete, set `deleted_at` with `is_deleted=1` in same statement. |
| `members.gender` | SQL stores `Nam/Nữ/Khác`; Java uses `MALE/FEMALE/OTHER`. Implement explicit mapping. |

Demo account passwords are PBKDF2-HMAC-SHA256 hashes of the **test-only** password already documented in the repository's SQL setup README. Format: `pbkdf2_sha256$210000$<salt-hex>$<digest-hex>`. Java `PasswordUtil` must implement this format to authenticate. Do not deploy these demo accounts or hashes to production or expose a demo login publicly.

### Scope and caveats

- **Only 6 Must tables:** `accounts`, `members`, `plan_types`, `memberships`, `payment_transactions`, `visits`.
- **SRS 3.4 / CN-09:** `plan_types` permits multiple different plan names with the same duration (`1 DAY`, `1 MONTH`, `6 MONTH`, `1 YEAR`). `CK_plan_types_duration` restricts these four combinations; `UQ_plan_types_name` restricts normalized names. Do **not** create `UQ_plan_types_duration`. The four seeded plan rows are examples, not the maximum plan count.
- `VisitChanges`/`AuditLogs`, manual edit/cancel visit, timer-based auto-close, future start-date selection, and advanced concurrent confirmations are outside the v1.1 Must scope (FUT-01–04).
- Required cross-table application transactions (create Membership + PENDING transaction; confirm PAID + schedule; lazy auto-close) are **not** implemented by these DDL/seed scripts.
- `UNIQUE(membership_id)` enforces *at most* one PaymentTransaction per Membership; the service transaction ensures both records are created together.
- `NEXT VALUE FOR dbo.seq_paid_sequence` is consumed by seeded PAID rows so the next app confirmation receives a later value. A SQL Server sequence does not guarantee commit ordering under arbitrary concurrency.
- The scripts were **statically checked** but have **not been executed on a SQL Server 2022 instance in this environment**.

**Repository source used:** `MrDonut12/gym-management` (branch `main`), `docs/sql_scripts/GymDB_Full_Setup.sql` and Java DAO skeletons. New sample names/contacts are synthetic.
