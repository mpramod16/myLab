/**
 * Created by pmadishe on 20-08-2018.
 */

use tekhealth;

db.createUser(
    {
        user: "admin",
        pwd: "manage",
        roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
    }
);

db.createCollection("test");