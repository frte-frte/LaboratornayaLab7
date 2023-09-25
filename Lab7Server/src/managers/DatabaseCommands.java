package managers;

public class DatabaseCommands {
    public static final String createAllTables = """
            CREATE TYPE FURNISH AS ENUM(
                'DESIGNER',
                'NONE',
                'FINE',
                'BAD',
                'LITTLE'
            );
            CREATE TYPE TRANSPORT AS ENUM(
                'FEW',
                'NONE',
                'LITTLE',
                'NORMAL',
                'ENOUGH'
            );
            CREATE TYPE VIEW AS ENUM(
                'STREET',
                'PARK',
                'NORMAL',
                'GOOD',
                'TERRIBLE'
            );
            CREATE TABLE IF NOT EXISTS coordinates(
                id                      SERIAL PRIMARY KEY,
                x                       BIGINT NOT NULL,
                y                       BIGINT NOT NULL
            );
            ALTER TABLE coordinates
                ADD CONSTRAINT unique_coordinates UNIQUE (x, y);
                
            CREATE TABLE IF NOT EXISTS house(
                id                      SERIAL PRIMARY KEY,
                name                    VARCHAR(40) NOT NULL,
                year                    BIGINT NOT NULL,
                numberOfFlatsOnFloor    BIGINT NOT NULL
            );
            ALTER TABLE house
                ADD CONSTRAINT unique_house UNIQUE (name, year, numberOfFlatsOnFloor);
                
            CREATE TABLE IF NOT EXISTS flat(
                id                      SERIAL PRIMARY KEY,
                name                    VARCHAR(40) NOT NULL,
                id_coordinates          INT NOT NULL REFERENCES coordinates (id) ON DELETE CASCADE,
                creationDate            TIMESTAMP NOT NULL DEFAULT NOW(),
                area                    DOUBLE NOT NULL,
                numberOfRooms           BIGINT NOT NULL,
                furnish                 FURNISH NOT NULL,
                view                    VIEW NOT NULL,
                transport               TRANSPORT NOT NULL,
                id_house                INT NOT NULL REFERENCES house (id) ON DELETE CASCADE,
                id_owner                INT NOT NULL REFERENCES flat_users (id) ON DELETE CASCADE             
            );
            CREATE TABLE IF NOT EXISTS flat_users(
                id                      SERIAL PRIMARY KEY,
                login                   TEXT UNIQUE NOT NULL,
                password                TEXT NOT NULL
            );
            """;

    /**
     * Команды для получения объектов
     */
    public static final String getAllObjects = """
            SELECT * FROM flat;
            """;
    public static final String getCoordinates = """
            SELECT * FROM coordinates WHERE id = ?;
            """;
    public static final String getHouse = """
            SELECT * FROM house WHERE id = ?;
            """;
    public static final String getUser = """
            SELECT * FROM flat_users WHERE login = ?;
            """;
    public static final String getUserById = """
            SELECT * FROM flat_users WHERE id = ?;
            """;
    public static final String getFlat = """
            SELECT * FROM flat
            WHERE id_owner = ?;
            """;
    public static final String getAllUsers = """
            SELECT * FROM flat_users;
            """;
    /**
     * Команды для удаления объектов
     */
    public static final String deleteAllObjects = """
            DELETE FROM flat
            WHERE id_owner = ?;
            """;
    public static final String deleteObject = """
            DELETE FROM flat
            WHERE id = ?;
            """;
    /**
     * Команды для записи объектов
     */
    public static final String addFlat = """
            INSERT INTO flat(name, id_coordinates, creationDate, area, numberOfRooms, furnish, view, transport,
            id_house, id_owner) VALUES (?, ?, ?, ?, ?, CAST(? AS FURNISH), CAST(? AS VIEW), CAST(? AS TRANSPORT), ?, ?)
            RETURNING id;
           """;
    public static final String addHouse = """
           INSERT INTO house (name, year, numberOfFlatsOnFloor) VALUES (?, ?, ?)
           ON CONFLICT (name, year, numberOfFlatsOnFloor) DO UPDATE SET name = house.name, year = house.year,
           numberOfFlatsOnFloor = house.numberOfFlatsOnFloor
           RETURNING id;
            """;
    public static final String addCoordinates = """
            INSERT INTO coordinates (x, y) VALUES (?, ?)
            ON CONFLICT (x, y) DO UPDATE SET x = coordinates.x, y = coordinates.y
            RETURNING id;
            """;
    public static final String addUser = """
            INSERT INTO flat_users(login, password) VALUES (?, ?)
            RETURNING id;
            """;
    public static final String addFlatWithId = """
            INSERT INTO flat(id, name, id_coordinates, creationDate, area, numberOfRooms, furnish, view, transport,
            id_house, id_owner) VALUES (?, ?, ?, ?, ?, ?, CAST(? AS FURNISH), CAST(? AS VIEW), CAST(? AS TRANSPORT), ?, ?)
            """;
}
