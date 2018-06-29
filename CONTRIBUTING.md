# Contributing guidelines

## Pull Request Checklist

Before sending your pull requests, make sure you followed this list.

- Run & pass [Unit Tests](CONTRIBUTING.md#running-unit-tests).
- Read [contributing guidelines](CONTRIBUTING.md).
- Read [Code of Conduct](CODE_OF_CONDUCT.md).
- Ensure you have signed the [Contributor License Agreement (CLA)](https://cla.developers.google.com/).
- Check if my changes are consistent with the [guidelines](CONTRIBUTING.md#general-guidelines-and-philosophy-for-contribution).
- Changes are consistent with the [Coding Style](CONTRIBUTING.md#coding-style-for-languages).
- Run & pass [Unit Tests](CONTRIBUTING.md#running-unit-tests) ! ! !.

## How to become a contributor and submit your own code

### Contributor License Agreements

We'd love to accept your patches! Before we can take them, we have to jump a couple of legal hurdles.

Please fill out either the individual or corporate Contributor License Agreement (CLA).

  * If you are an individual writing original source code and you're sure you own the intellectual property, then you'll
  need to sign an [individual CLA](https://code.google.com/legal/individual-cla-v1.0.html).
  * If you work for a company that wants to allow you to contribute your work, then you'll need to sign a
  [corporate CLA](https://code.google.com/legal/corporate-cla-v1.0.html).

Follow either of the two links above to access the appropriate CLA and instructions for how to sign and return it. Once
 we receive it, we'll be able to accept your pull requests.

***NOTE***: Only original source code from you and other people that have signed the CLA can be accepted into the main repository.

### Contributing code

If you have improvements to InterProxy, send us your pull requests! For those
just getting started, Github has a [howto](https://help.github.com/articles/using-pull-requests/).

InterProxy team members will be assigned to review your pull requests. Once the pull requests are approved and pass
 continuous integration checks, we will merge the pull requests.
For some pull requests, we will apply the patch for each pull request to our internal version control system first, and
 export the change out as a new commit later, at which point the original pull request will be closed. The commits in the
 pull request will be squashed into a single commit with the pull request creator as the author. These pull requests will
 be labeled as pending merge internally.

If you want to contribute but you're not sure where to start, take a look at the
[issues with the "contributions welcome" label](https://github.com/ilucatero/interproxy/labels/contributions%20welcome).
These are issues that we believe are particularly well suited for outside
contributions, often because we probably won't get to them right now. If you
decide to start on an issue, leave a comment so that other people know that
you're working on it. If you want to help out, but not alone, use the issue
comment thread to coordinate.

### Contribution guidelines and standards

Before sending your pull request for
[review](https://github.com/ilucatero/interproxy/pulls),
make sure your changes are consistent with the guidelines and follow the
InterProxy coding style.

#### General guidelines and philosophy for contribution

* Include unit tests when you contribute new features, as they help to
  a) prove that your code works correctly, and b) guard against future breaking
  changes to lower the maintenance cost.
* Bug fixes also generally require unit tests, because the presence of bugs
  usually indicates insufficient test coverage.
* Keep API compatibility in mind when you change code in core InterProxy,
  e.g., code in [InterProxy/core](https://github.com/ilucatero/interproxy/tree/master/inter-proxy-core) and 
  [InterProxy/web](https://github.com/ilucatero/interproxy/tree/master/inter-proxy-web).
  InterProxy has reached version 1 and hence cannot make
  non-backward-compatible API changes without a major release. Reviewers of your
  pull request will comment on any API compatibility issues.
* When you contribute a new feature to InterProxy, the maintenance burden is (by
  default) transferred to the InterProxy team. This means that benefit of the
  contribution must be compared against the cost of maintaining the feature.
* Full new features (e.g., a new op implementing a cutting-edge algorithm)
  typically will live in
  [InterProxy/contrib](https://github.com/ilucatero/interproxy/contrib)
  to get some airtime before decision is made regarding whether they are to be
  migrated to the core.

#### Licensing

Include a license at the top of new files.

* [Java license example](https://github.com/ilucatero/interproxy/blob/master/inter-proxy-core/src/main/java/io/interproxy/core/ProxyHostsManager.java)
* [Python license example]()
* [Bash license example]()
* [HTML license example](https://github.com/ilucatero/interproxy/blob/master/inter-proxy-web/src/main/resources/static/pages/index.html)
* [JavaScript/TypeScript license example]()



#### Coding style for languages

* [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* [Google Python Style Guide](https://google.github.io/styleguide/pyguide.html)
* [Google JavaScript Style Guide](https://google.github.io/styleguide/jsguide.html)
* [Google Shell Style Guide](https://google.github.io/styleguide/shell.xml)




#### Running unit tests

There are two ways to run InterProxy unit tests.

1. Using tools and libraries installed directly on your system.

   Refer to the
   [Web developer Dockerfile](https://github.com/InterProxy/InterProxy/blob/master/InterProxy/tools/docker/Dockerfile.one) and
   [Test developer Dockerfile](https://github.com/InterProxy/InterProxy/blob/master/InterProxy/tools/docker/Dockerfile.other)
   for the required packages. Alternatively, use the said
   [Docker images](https://hub.docker.com/r/InterProxy/InterProxy/tags/), e.g.,
   `InterProxy/InterProxy:nightly-one` and `InterProxy/InterProxy:nightly-other`
   for development to avoid installing the packages directly on your system.

   Once you have the packages installed, you can run a specific unit test in
   bazel by doing as follows:

   If the tests are to be run on GPU, add CUDA paths to LD_LIBRARY_PATH and add
   the `cuda` option flag

   ```bash
   export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:/usr/local/cuda/lib64:/usr/local/cuda/extras/CUPTI/lib64:$LD_LIBRARY_PATH"

   export flags="--config=opt --config=cuda -k"
   ```

   For example, to run all tests under InterProxy/python, do:

   ```bash
   bazel test ${flags} //InterProxy/python/...
   ```

2. Using [Docker](https://www.docker.com) and InterProxy's CI scripts.

   ```bash
   # Install Docker first, then this will build and run cpu tests
   InterProxy/tools/ci_build/ci_build.sh CPU bazel test //InterProxy/...
   ```

   See
   [InterProxy Builds](https://github.com/InterProxy/InterProxy/tree/master/InterProxy/tools/ci_build) for details.
