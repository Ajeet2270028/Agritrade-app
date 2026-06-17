package com.agritrade.agritrade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class AgritradeApplication {

	public static void main(String[] args) throws Exception {

		String yearMonth = new SimpleDateFormat("yyyy_MMMM").format(new Date());
		String logFolder = "AppLogs/"+yearMonth;
		Files.createDirectories(Paths.get(logFolder));

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String logfileName = logFolder + "/"+ date + ".log";
		System.setProperty("logging.file.name",logfileName);


		SpringApplication.run(AgritradeApplication.class, args);
		log.info("Agritrade Application Started Successfully");
//		log.info("Log File Location : {}", logfileName);
	}

}
