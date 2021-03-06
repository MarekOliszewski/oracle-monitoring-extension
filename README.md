# AppDynamics Oracle Database - Monitoring Extension

This extension works only with the standalone machine agent.

##Use Case
The Oracle Database is an object-relational database management system. 

The Oracle Database monitoring extension captures performance metrics from Oracle databases (version 10g and above) and displays them in AppDynamics. It retrieves metrics from the data dictionary and groups them in three categories:

-   Activity: Throughput over the last minute, such as transactions, SQL executes, I/O reads and writes, average active sessions, etc.
-   Resource Utilization: What database resources are currently in use (sessions, open cursors, shared pool, etc).
-   Efficiency: Ratios and response times as indicators of the instance's efficiency.

##Prerequisite
The Oracle DB extension needs an Oracle user account on every Oracle instance that is to be monitored. You might use an existing account with appropriate rights; however, a dedicated account will be a better solution in terms of security and manageability.
    -   Example script for account creation (run this with a DBA user):

```
            CREATE USER appdynamics IDENTIFIED BY oracle;
            GRANT CREATE SESSION TO appdynamics;
            GRANT SELECT ANY DICTIONARY TO appdynamics;
```
##Installation
1. To build from source, clone this repository and run 'mvn clean install'. This will produce a OracleDBMonitor-VERSION.zip in the target directory. Alternatively, download the latest release archive from [Github](https://github.com/Appdynamics/oracle-monitoring-extension/releases).
2. Unzip the file OracleDBMonitor-[version].zip into `<MACHINE_AGENT_HOME>/monitors/`.
3. In the newly created directory "OracleDBMonitor", edit the config.yml configuring the parameters (See Configuration section below).
4. Restart the machineagent
5. In the AppDynamics Metric Browser, look for: Application Infrastructure Performance  | \<Tier\> | Custom Metrics | Oracle Server | SID.
6. If you're monitoring multiple Oracle DB instances, follow the above steps for every Oracle instance (SID) that you want to monitor.

## Configuration ##
Note : Please make sure to not use tab (\t) while editing yaml files. You may want to validate the yaml file using a [yaml validator](http://yamllint.com/)

1. Configure the Oracle DB parameters by editing the config.yml file in `<MACHINE_AGENT_HOME>/monitors/OracleDBMonitor/`. Specify the host, port, username, password and sid of the Oracle DB instance.

   For eg.
   ```
        # Oracle DB connection params
        host: "localhost"
        port: 49161
        sid: "xe"
        username: "system"
        password: "oracle"
        # prefix used to show up metrics in AppDynamics
        metricPathPrefix:  "Custom Metrics|ORACLE Server|"

   ```

3. Configure the path to the config.yml file by editing the <task-arguments> in the monitor.xml file in the `<MACHINE_AGENT_HOME>/monitors/OracleDBMonitor/` directory. Below is the sample

     ```
     <task-arguments>
         <!-- config file-->
         <argument name="config-file" is-required="true" default-value="monitors/OracleDBMonitor/config.yml" />
          ....
     </task-arguments>
    ```

##Metrics
Here is a summary of the collected metrics. Complete documentation of Oracle's metrics can be found at [http://docs.oracle.com/cd/E11882\_01/server.112/e17110/waitevents.htm\#REFRN101](http://docs.oracle.com/cd/E11882_01/server.112/e17110/waitevents.htm#REFRN101).

AppDynamics displays metric values as integers. Some metrics are therefore scaled up by a factor of 100 for a better display of low values (e.g. between 0 and 2).


<table>
<tr><td><strong>Metric Class</strong></td><td><strong>Description</strong></td>

<tr>
<td>Activity</td>
<td>

<table>
    <tr><td><strong>Metric</strong></td><td><strong>Description</strong></td>
    <tr><td>Active Sessions Current</td><td>Number of active sessions at the point in time when the snapshot was taken.</td></tr>
    <tr><td>Average Active Sessions</td><td>Average number of active sessions within the last 60 s. This is maybe the single most important DB load metric and a good starting point for a drill-down.</td></tr>
    <tr><td>Average Active Sessions per logical CPU (\*100)</td><td>This shows the average load the database imposes on each logical CPU (i.e. cores or hyperthreads). Values above 100 (more than 1 waiting DB session per CPU) indicate a higher demand for resources than the host can satisfy. This often marks the beginning of quickly rising response times.</td></tr>
    <tr><td>Current OS Load</td><td>Host CPU load, when available.</td></tr>
    <tr><td> DB Block Changes Per Sec</td><td>Database blocks changed in the buffer cache.</td></tr>
    <tr><td>DB Block Changes Per Txn</td><td>Database blocks changed in the buffer cache per SQL transaction.</td></tr>
    <tr><td>DB Block Gets Per Sec</td><td>Database blocks read from the buffer cache.</td></tr>
    <tr><td>DB Block Gets Per Txn</td><td>Database blocks read from the buffer cache per SQL transaction.</td></tr>
    <tr><td>Executions Per Sec</td><td>SQL executions/s</td></tr>
    <tr><td>Executions Per Txn</td><td>SQL executions per SQL transaction.</td></tr>
    <tr><td>I/O Megabytes per Second</td> <td></td></tr>
    <tr><td>Logical Reads Per Sec</td><td>Logical reads are comprised of database block reads from the buffer cache + physical reads from disk.</td></tr>
    <tr><td>Logons Per Sec</td> <td></td></tr>
    <tr><td>Physical Reads Per Sec</td><td>Database blocks read from disk.</td></tr>
    <tr><td>Physical Read Total Bytes Per Sec</td> <td></td></tr>
    <tr><td>Physical Write Total Bytes Per Sec</td> <td></td></tr>
    <tr><td>Txns Per Sec</td><td>Transactions per second.</td> <td></td></tr>
</table>
  
</td>
</tr>

<tr>
<td>Wait Class Breakdown <a name = "waitclassbreakdown"></a></td>
<td>Shows average active sessions per each wait class. Typically, the top wait classes are "CPU" and "User I/O". A shift to other wait classes is a good pointer for further   nvestigation (e.g., of network latency issues). Wait classes are documented in the Oracle Database Reference. See here: [http://docs.oracle.com/cd/E11882\_01/server.112/e17110/waitevents001.htm\#BGGHJGII](http://docs.oracle.com/cd/E11882_01/server.112/e17110/waitevents001.htm#BGGHJGII)</td>
</tr>


<tr>
  <td>Efficiency<a name = "efficiency"></a></td>
  <td>
  
<table>
    <tr><td><strong>Metric</strong></td><td><strong>Description</strong></td></tr>
    <tr><td>Database CPU Time Ratio</td><td>Percentage of CPU time against all database time.</td></tr>
    <tr><td>Database Wait Time Ratio</td><td>Complementary to "Database CPU Time Ratio" (percentage of non-CPU waits).</td></tr>
    <tr><td>Memory Sorts Ratio</td><td>Percentage of sort operations that were done in RAM (as opposed to disk).</td></tr>
    <tr><td>Execute Without Parse Ratio</td><td>Percentage of (soft and hard) parsed SQL against all executed SQL.</td></tr>
    <tr><td>Soft Parse Ratio</td><td>Ratio of soft parses to hard parses.</td></tr>
    <tr><td>Response Time Per Txn (ms)</td> <td></td></tr>
    <tr><td>SQL Service Response Time (ms) <td></td> </tr>
</table>
    
  </td>
</tr>

<tr><td>Resource Utilization<a name="resourceutilization"></a></td>
<td>

<table>
    <tr><td><strong>Metric</strong></td><td><strong>Description</strong></td></tr>
    <tr><td> \# of logical CPUs</td><td>Observation for informational purpose. This count is used, among others, for the metric "Average Active Sessions per logical CPU".</td>
    <tr><td>Total Sessions</td><td>Count of all database sessions at the time the snapshot was taken.</td>
    <tr><td>% of max sessions</td><td>Open sessions vs. DB parameter "sessions".</td>
    <tr><td>% of max open cursors</td><td>Maximum count of open cursors in a session vs. DB parameter "open\_cursors".</td>
    <tr><td>Shared Pool Free %</td> <td></td>
    <tr><td>Temp Space Used (MB)</td><td>Amount of used temporary tablespace.</td>
    <tr><td>Total PGA Allocated (MB)</td><td>Amount of RAM used for sorts, hashes and the like.
  </table>
  </td>
</tr>

</table>


##Oracle Licensing
The metrics in the supplied code are retrieved from

```
-   v$session
-   v$sesstat
-   v$sysmetric
-   v$system_wait_class
-   v$waitclassmetric
```

all of which are, to the author's knowledge, not subject to additional
licensing of the Oracle Diagnostics Pack. See Oracle's "Options and
Packs" documentation:
[http://docs.oracle.com/cd/E11882\_01/license.112/e10594/options.htm\#CIHIHDDJ](http://docs.oracle.com/cd/E11882_01/license.112/e10594/options.htm#CIHIHDDJ)

If you plan on extending this code using data dictionary views of the
Diagnostics Pack (e.g., DBA\_HIST\_% views), you might want to make use
of the argument "ash\_licensed" in monitor.xml to easily en-/disable
usage of such code.

##Contributing
Always feel free to fork and contribute any changes directly here on GitHub.

##Community
Find out more in the [AppSphere](http://appsphere.appdynamics.com/t5/Extensions/Oracle-Database-Monitoring-Extension/idi-p/835) community.

##Support
For any questions or feature request, please contact [AppDynamics Center of Excellence](mailto:help@appdynamics.com).
