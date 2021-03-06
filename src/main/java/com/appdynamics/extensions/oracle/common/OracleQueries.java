/**
 * Copyright 2015 AppDynamics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appdynamics.extensions.oracle.common;

public class OracleQueries {
    public static final String sessions = "SELECT \'Sessions\', count(*) from v$session";
    public static final String percentOfMaxSessions = "SELECT \'% of max sessions\', a.cnt / b.cpus * 100 AS pct_max_sessions FROM ( SELECT COUNT(*) cnt FROM v$session ) a, ( SELECT value AS cpus FROM v$parameter WHERE name=\'sessions\') b";
    public static final String percentOfMaxOpenCursors = "SELECT \'% of max open cursors\', a.crs / b.max_crs * 100 as pct_open_cur FROM ( SELECT MAX(a.value) AS crs from v$sesstat a, v$statname b where a.statistic# = b.statistic# AND b.name = \'opened cursors current\' ) a, ( select value AS max_crs FROM v$parameter WHERE name=\'open_cursors\' ) b";
    public static final String activeUserSessions = "SELECT \'Active User Sessions\', COUNT(*) FROM v$session WHERE status=\'ACTIVE\' AND username IS NOT NULL";
    public static final String avgActiveSessionsPerLogicalCPU = "SELECT 'Average Active Sessions per logical CPU', a.value / b.cpus AS aas_per_cpu FROM (SELECT value FROM v$sysmetric WHERE group_id = 2 AND metric_name = 'Average Active Sessions') a, (SELECT value AS cpus FROM v$parameter WHERE name=\'cpu_count\') b";

    public static final String sysMetrics = "SELECT metric_name, value FROM v$sysmetric WHERE group_id = 2 AND metric_name IN ('Average Active Sessions', 'Current OS Load', 'Database CPU Time Ratio', 'Database Wait Time Ratio', 'DB Block Changes Per Sec', 'DB Block Changes Per Txn', 'DB Block Gets Per Sec', 'DB Block Gets Per Txn', 'Executions Per Sec', 'Executions Per Txn', 'I/O Megabytes per Second', 'Logical Reads Per Sec', 'Physical Reads Per Sec', 'Memory Sorts Ratio', 'Physical Read Total Bytes Per Sec', 'Physical Write Total Bytes Per Sec', 'Shared Pool Free %', 'Execute Without Parse Ratio', 'Soft Parse Ratio', 'Temp Space Used', 'Total PGA Allocated', 'Response Time Per Txn', 'SQL Service Response Time') ORDER BY metric_name";

    public static final String waitClassBreakDownMetrics = "SELECT 'Wait Class Breakdown|'||wait_class, ROUND(aas, 2) FROM(SELECT n.wait_class, m.time_waited/m.INTSIZE_CSEC AAS FROM v$waitclassmetric m, v$system_wait_class n WHERE m.wait_class_id=n.wait_class_id AND n.wait_class != 'Idle' UNION ALL SELECT 'CPU', value/100 AAS FROM v$sysmetric WHERE metric_name = 'CPU Usage Per Sec' AND group_id = 2)";

    public static final String [] queries = {sessions, activeUserSessions, percentOfMaxOpenCursors, percentOfMaxSessions, sysMetrics,
            avgActiveSessionsPerLogicalCPU, waitClassBreakDownMetrics};
}
