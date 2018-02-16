react-over-play
============================

A Play2 (Scala) controller that allows a React.js app to be served up.


### Installation

Bring in the library by adding the following to your ```build.sbt```. 

  - The release repository: 

```
   resolvers ++= Seq(
     "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven"
   )
```
  - The dependency itself: 

```
   libraryDependencies ++= Seq(
     "com.themillhousegroup" %% "react-over-play" % "0.1.3"
   )

```

### Usage

Once you have __react-over-play__ added to your project, it just takes two steps to get it working.

##### Build your React app such that its artifacts are under `<PROJECT_ROOT>/public/`

Say your React app has its sources in `<PROJECT_ROOT>/client`, you can accomplish this by having a `package.json` in `<PROJECT_ROOT>` that looks like this:
   
```
{
        "name": "build-frontend-client",
        "engines": {
                "node": "6.3.1"
        },
        "scripts": {
                "build-dir": "cd $DIR && npm install && npm run build && cd ..",
                "build": "DIR=client npm run build-dir",
                "deploy-dir": "cp -a $DIR/build/. public/$DIR",
                "deploy": "DIR=client npm run deploy-dir",
                "postinstall": "npm run build && npm run deploy && echo 'Client built!'"
        }
}
```

(Note this is a _separate, independent_ `package.json` to the one in `<PROJECT_ROOT>/client`).

Now you can run `npm install` in `<PROJECT_ROOT>` to get everything in position. 

After running, you should have a `client` directory under `<PROJECT_ROOT>/public/` that contains all your React app's artifacts (e.g. `index.html`, `app.js`, etc).
 
Note that you could potentially "host" _multiple_ React apps (e.g. `client2`, `client3`) this way!

##### Add fallthrough routes to your Play app

Again, assuming the React app is in a directory called `client` in the `/public` directory, edit your `conf/routes` file _at the very bottom_ to add: 

```
##### Last of all, fall through to the React app #####
GET         /list-assets        @com.themillhousegroup.react.FrontEndServingController.listPhysicalAssets
GET         /        @com.themillhousegroup.react.FrontEndServingController.frontEndPath(clientDir="client", path="index.html", indexFile="client/index.html")
GET         /*file        @com.themillhousegroup.react.FrontEndServingController.frontEndPath(clientDir="client", file, indexFile="client/index.html")

```

__Note__ the `list-assets` endpoint is of course optional, but can be useful for diagnostic purposes.

### Further info

http://blog.themillhousegroup.com/2017/07/the-crap-stack-part-3-front-end-routes.html

