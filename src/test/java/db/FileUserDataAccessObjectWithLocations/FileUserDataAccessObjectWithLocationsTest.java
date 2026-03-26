//package FileUserDataAccessObjectWithLocations;
//
//import data_access.FileUserDataAccessObjectWithLocations;
//import entity.user.User;
//import entity.user.UserFactory;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FileUserDataAccessObjectWithLocationsTest {
//
//    /**
//     * 辅助方法：创建一个临时 CSV 文件路径。
//     * 每个测试用自己的 temp file，互不影响。
//     */
//    private File createTempCsvFile() throws IOException {
//        File tempFile = File.createTempFile("users_test_", ".csv");
//        // 让 JVM 结束时自动删掉
//        tempFile.deleteOnExit();
//        // 清空内容（有些实现 tempFile 可能默认 size > 0）
//        try (FileWriter fw = new FileWriter(tempFile, false)) {
//            // 写空内容，保持 size=0
//        }
//        return tempFile;
//    }
//
//    @Test
//    public void testConstructorCreatesHeaderWhenFileEmpty() throws Exception {
//        // Arrange
//        File tempFile = createTempCsvFile();
//        String path = tempFile.getAbsolutePath();
//        UserFactory userFactory = new UserFactory();
//
//        // Act：构造 DAO，会检测 length==0，然后调用 save() 写 header
//        FileUserDataAccessObjectWithLocations dao =
//                new FileUserDataAccessObjectWithLocations(path, userFactory);
//
//        // Assert：检查文件第一行是 header
//        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
//            String header = reader.readLine();
//            assertEquals("username,password,locations,token", header);
//            // 没有用户时，第二行应该是 null
//            assertNull(reader.readLine());
//        }
//    }
//
//    @Test
//    public void testSaveAndGetUser() throws Exception {
//        // Arrange
//        File tempFile = createTempCsvFile();
//        String path = tempFile.getAbsolutePath();
//        UserFactory userFactory = new UserFactory();
//
//        FileUserDataAccessObjectWithLocations dao =
//                new FileUserDataAccessObjectWithLocations(path, userFactory);
//
//        List<String> locations = Arrays.asList("Toronto", "Montreal");
//        String username = "alice";
//        String password = "password123";
//        String token = "token_abc";
//
//        User user = userFactory.create(username, password, locations, token);
//
//        // Act
//        dao.save(user);
//
//        // Assert (内存中的 map)
//        assertTrue(dao.existsByName(username));
//        User saved = dao.get(username);
//        assertNotNull(saved);
//        assertEquals(username, saved.getName());
//        assertEquals(password, saved.getPassword());
//        assertEquals(locations, saved.getLocations());
//        assertEquals(token, saved.getToken());
//
//        // Assert (检查 CSV 文件内容)
//        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
//            String header = reader.readLine();
//            assertEquals("username,password,locations,token", header);
//
//            String dataLine = reader.readLine();
//            assertNotNull(dataLine, "Data line should not be null");
//
//            // 预期格式：username,password,location1;location2,token
//            String expectedLine = String.format("%s,%s,%s,%s",
//                    username,
//                    password,
//                    String.join(";", locations),
//                    token);
//
//            assertEquals(expectedLine, dataLine);
//            assertNull(reader.readLine(), "Only one user row expected");
//        }
//    }
//
//    @Test
//    public void testReloadFromExistingFile() throws Exception {
//        // Arrange：先用一个 DAO 写入用户，再用同一个文件重新 new 一个 DAO 读取
//        File tempFile = createTempCsvFile();
//        String path = tempFile.getAbsolutePath();
//        UserFactory userFactory = new UserFactory();
//
//        // 第一次：写入
//        FileUserDataAccessObjectWithLocations dao1 =
//                new FileUserDataAccessObjectWithLocations(path, userFactory);
//
//        List<String> locations = Arrays.asList("Toronto", "Vancouver");
//        User user = userFactory.create("bob", "pw123", locations, "token_xyz");
//        dao1.save(user);
//
//        // 第二次：重新 new DAO，从文件里把数据读回 accounts map
//        FileUserDataAccessObjectWithLocations dao2 =
//                new FileUserDataAccessObjectWithLocations(path, userFactory);
//
//        // Act
//        User loaded = dao2.get("bob");
//
//        // Assert
//        assertNotNull(loaded, "User should be loaded from existing CSV");
//        assertEquals("bob", loaded.getName());
//        assertEquals("pw123", loaded.getPassword());
//        assertEquals(locations, loaded.getLocations());
//        assertEquals("token_xyz", loaded.getToken());
//    }
//
//    @Test
//    public void testSetAndGetCurrentUsername() throws Exception {
//        // Arrange
//        File tempFile = createTempCsvFile();
//        String path = tempFile.getAbsolutePath();
//        UserFactory userFactory = new UserFactory();
//
//        FileUserDataAccessObjectWithLocations dao =
//                new FileUserDataAccessObjectWithLocations(path, userFactory);
//
//        // Act
//        dao.setCurrentUsername("charlie");
//
//        // Assert
//        assertEquals("charlie", dao.getCurrentUsername());
//    }
//}
