# Contributing

Check out ways to contribute to ING Open Banking SDK:

## Feature requests

When you have an idea on how we could improve, please check our [discussions](https://github.com/ing-bank/ing-open-banking-sdk/discussions) to see if there are similar ideas or feature requests. If there are none, please [start](https://github.com/ing-bank/ing-open-banking-sdk/discussions/new) your feature request as a new discussion topic. Add the title `[Feature Request] My awesome feature` and a description of what you expect from the improvement and what the use case is.

## Existing components: we love pull requests ♥

Help out the whole community by sending your merge requests and issues.
Check out how to set it up:

Setup:

```bash
# Clone the repo:
git clone https://github.com/ing-bank/ing-open-banking-sdk.git
cd ing-open-banking-sdk

# Create a branch for your changes
git checkout -b fix/java-npe
```

Make sure everything works as expected:

```bash
mvn clean test
```

Create a Pull Request:

- At <https://github.com/ing-bank/ing-open-banking-sdk> click on fork (at the right top)

```bash
# add fork to your remotes
git remote add fork git@github.com:<your-user>/ing-open-banking-sdk.git

# push new branch to your fork
git push -u fork fix/java-npe
```

- Go to your fork and create a Pull Request :).

Some things that will increase the chance that your merge request is accepted:

- Write test scripts.
- Write a [good commit message](https://www.conventionalcommits.org/).
