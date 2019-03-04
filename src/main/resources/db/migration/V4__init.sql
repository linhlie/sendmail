ALTER TABLE `flyway_demo`.`sendemail`
ADD COLUMN `filename` VARCHAR(100) NULL AFTER `sendtime`;