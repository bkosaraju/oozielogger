INSERT INTO WORKFLOW_JOB_LOG(id,
appName,
startTime,
endTime,
`status`,
errorCode,
errorMessage,
parentId,
appType,
`user`,
msgType,
eventStatus
)
values (?,?,?,?,?,?,?,?,?,?,?,?);
