package com.sist.web.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Qualifier("vectorDataSource")
    private final DataSource vectorDataSource;

    public TestController(@Qualifier("vectorDataSource") DataSource vectorDataSource) {
        this.vectorDataSource = vectorDataSource;
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> testConnection(){
        Map<String, Object> result = new LinkedHashMap<>();

        String sql = """
                            SELECT
                                (SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector') AS ext_count,
                                (SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'course_vector') AS table_count,
                                (SELECT COUNT(*) FROM course_vector) AS row_count
                """;

        try(
                Connection conn = vectorDataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ){
            if (rs.next()) {
                result.put("connected", true);
                result.put("vectorExtensionInstalled", rs.getInt("ext_count") > 0);
                result.put("tableExists", rs.getInt("table_count") > 0);
                result.put("rowCount", rs.getInt("row_count"));
                result.put("status", "OK");
            }
            return ResponseEntity.ok(result);
        }catch (SQLException e){
            e.printStackTrace();
            result.put("status", "FAIL");
            result.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
