# TBC Node Example

This is an example of using the [To Be Continued](http://github.com/gregspurrier/to-be-continued) library in a ClojureScript program targeting Node.js.

The result is a command-line utility that takes a GitHub username as its argument and outputs a summary of that user's projects along with who has contributed to each.

NOTE: for the sake of simplicity, this example uses GitHub's unauthenticated API which has a very tight [rate limit](http://developer.github.com/v3/#rate-limiting) of 60 queries per IP address per hour.

## Usage

    lein deps
    lein cljsbuild once
    node lib/tbc-node-example.js <github_username>

## License

Copyright (c) 2012 Greg Spurrier

Distributed under the MIT License. See LICENSE.txt for the details.
