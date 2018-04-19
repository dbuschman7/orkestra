---
layout: docs
title:  "Directories"
position: 4
---

# Directories

When playing with files we often need to change directories, so you might wonder can we do that in Orchestra!
First of all this is now time to explain what the `implicit workDir`. This implicit is used for example by the function
`sh()` as seen in [Shells](shells.html) to know where to run the shell script.  
So if we'd like to changing directory we would use the function `dir` that changes the current directory only for the
scope of the function passed to it:
```tut:silent
import scala.concurrent.Await
import scala.concurrent.duration._
import com.drivetribe.orchestra._
import com.drivetribe.orchestra.Dsl._
import com.drivetribe.orchestra.board._
// We import the directories DSL
import com.drivetribe.orchestra.filesystem.Directories._
import com.drivetribe.orchestra.job.JobRunner
import com.drivetribe.orchestra.model._
import com.drivetribe.orchestra.utils.Shells._

lazy val directoryJob = Job[() => Unit](JobId("directory"), "Directory")()
lazy val directoryJobRunner = JobRunner(directoryJob) { implicit workDir => () =>
  Await.result(for {
    // Create the directory subDir
    _ <- sh("mkdir subDir")
    // Moving in the subDir
    _ <- dir("subDir") { implicit workDir =>
      // Print the working directory
      sh("pwd")
    }
  } yield (), 1.second)
}
```
Note that we need to take this `implicit workDir =>` again in the function passed to `dir`. 

## LocalFile
When dealing with files in Scala we usually use the `java.io.File`. In Orchestra as we can change the current working
directory we need the relative `File` to be aware of it. This is why we have the `LocalFile` class that extends `File`
so that you can use it in Scala or Java libraries.  
Here is an example of how to use it:
```tut:silent
import com.drivetribe.orchestra.filesystem._

def createFile()(implicit workDir: Directory): Unit = { 
  dir("subDir") { implicit workDir =>
    assert(LocalFile("myFile").createNewFile(), "File was not able to be created")
  }
}
```