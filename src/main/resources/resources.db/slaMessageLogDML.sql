INSERT INTO SLA_LOG(id,
appName,
expectedStartTime,
expectedEndTime,
actualStartTime,
actualEndTime,
expectedDuration,
actualDuration,
nominalTime,
parentId,
appType,
`user`,
msgType,
eventStatus,
notificationMessage,
upstreamApps,
slaStatus
)
values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);