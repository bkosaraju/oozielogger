CREATE TABLE IF NOT EXISTS WORKFLOW_JOB_LOG (
id             VARCHAR(60),
appName 			VARCHAR(60),
startTime      BIGINT,
endTime   		BIGINT,
`status`       VARCHAR(10),
errorCode      VARCHAR(256),
errorMessage   VARCHAR(256),
parentId 		VARCHAR(60),
appType 			VARCHAR(60),
`user` 			VARCHAR(60),
msgType			VARCHAR(15),
eventStatus		VARCHAR(15),
LOADTIME       TIMESTAMP default CURRENT_TIME,
PRIMARY KEY(id,`status`)
);