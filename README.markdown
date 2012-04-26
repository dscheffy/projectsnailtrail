Project Snail Trail -- Android
------------------------------

**WARNING:** This code is not for mainstream consumption -- it's ugly, ill-documented, subject to major changes
poorly organized, etc.  It is very much under active construction and I thought it was a decent point in time
to start making some of that construction publicly visible.  Feel free to follow status of the project at its
official site -- [Project Snail Trail](http://www.projectsnailtrail.org) (which is itself a work in progress).

Among other things I need to reorganize/rename a many of the classes in the code.  As is they're all sitting in 
the same package.  I'm also still puzzling over just how much work is appropriate to put in a `BroadcastReceiver`
as opposed to kicking off a `Service`.  Is writing 20 bytes of data to a file appropriate?  I suppose it's 
relatively minimalistic and I'm overthinking things when I worry about the case where the system has to zero out
a block of bytes on the flash ram before flipping on the bits for those 20 or so odd bytes.  

I also really need to rename the repo itself -- at the moment it's on GitHub as projectsnailtrail, but really it's
just the android portion of the project -- should I make it a sub module?  Or just it's own project with an android
qualifier in the name?  These are the silly little questions that keep me up at night (not really).  

Dependencies
------------

* projectsnailtrail-lib (see below)
* Google API Add-ons (support for google maps)
 
I still haven't really quite grokked (I don't fully understand) how junit fits in with android projects, so for
simplicity sake I've split out a "lib" project which is a straight up java project (well, for now it only contains
java, but that could change in the future whenever I see a need for library support in other languages).  So far
this really just includes serialization implementations which will make sense to keep separate from the android
project as anybody who want's to deserialize or play with the deserialized object will want the ability to work
with them without having to write their own code.  

For more information on TrackPoints (which will likely one day be completely refactored and renamed) see the
as of yet non-existent documentation in the lib project itself.  

I know it's wrong, but for now I'm just including a built snapshot jar file in this android projects `libs` folder
and checking it into the git repo.  Maybe one day I'll figure out how to build out the default android build file
to include it as a dependency using `Ivy` or switch over to `Maven` for builds.  For now this approach works.

Building/Usage
--------------

One of these days I'll go through the step by step process of configuring a workstation from scratch (no android
dev environment), but for now I'll assume you're already familiar with building android projects -- if not you may
just want to skip to the [downloads](https://github.com/dscheffy/projectsnailtrail/downloads) section of this project
and download the ProjectSnailTrail-debug.apk file itself.  You'll need to enable packages signed with debug keys
on your device (google it if you don't know how).

One caveat for those building the project from source -- in order for the google maps to work, you'll need to replace
the `apiKey` in `res/layout/map_view.xml` with your own developer key.  If you haven't done this before you'll need
to set up a developer account and get a key that will work when signed with your own debug key (again, if you don't
know what I'm talking about, google it).  I'll try to document better how to do this in the future, but for now
I don't really expect anybody to be reading this -- again, this project isn't really ready for mainstream consumption...

Versions
--------

Are you kidding me?  This code is absolute crap -- hopefully one day soon it will be worthy of a 0.0.x version, but for 
now it's still just rapid prototype code (look through the history and you'll see only a week or so ago it was still
named HelloJefferson!)


