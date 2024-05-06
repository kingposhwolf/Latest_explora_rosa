package com.example.demo.Controllers.LocationDetection;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/location-track")
@AllArgsConstructor
public class LocationDetectionController {

    @PostMapping("/withIp")
    public ResponseEntity<Object> elocationTrack() throws IOException, GeoIp2Exception {
        
        File database = new File("src\\main\\resources\\Other_Helper_Files\\GeoLite2DB\\City\\GeoLite2-City_20240503\\GeoLite2-City.mmdb");
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName("213.180.203.246");
        CityResponse response = dbReader.city(ipAddress);

        return ResponseEntity.ok(response.getCountry().getName());
    }
}
