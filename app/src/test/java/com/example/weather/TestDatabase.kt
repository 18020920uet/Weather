//import androidx.room.testing.MigrationTestHelper
//import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
//import java.io.IOException
//
//@RunWith(AndroidJUnit4::class)
//class MigrationTest {
//    private val TEST_DB = "migration-test"
//
//    @Rule
//    val helper: MigrationTestHelper = MigrationTestHelper(
//        InstrumentationRegistry.getInstrumentation(),
//        MigrationDb::class.java.canonicalName,
//        FrameworkSQLiteOpenHelperFactory()
//    )
//
//    @Test
//    @Throws(IOException::class)
//    fun migrate1To2() {
//        var db = helper.createDatabase(TEST_DB, 1).apply {
//            // db has schema version 1. insert some data using SQL queries.
//            // You cannot use DAO classes because they expect the latest schema.
//            execSQL(...)
//
//            // Prepare for the next version.
//            close()
//        }
//
//        // Re-open the database with version 2 and provide
//        // MIGRATION_1_2 as the migration process.
//        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)
//
//        // MigrationTestHelper automatically verifies the schema changes,
//        // but you need to validate that the data was migrated properly.
//    }
//}