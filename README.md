# traveloci

## build

```
# mvn -DskipTests package
````

## install

Install target/traveloci.hpi using Manage Jenkins -> Manage Plugins -> Advanced -> Upload Plugin

## usage

New Job -> Build a free-style software project -> Build -> execute travelo task

### example

```
matrix:
  include:
  - rvm: default
    env: BEAKER_set="centos6-docker"
    dist: trusty
    bundler_args:
    script: sudo service docker restart ; sleep 10 && /opt/beaker/bin/rspec spec/acceptance/*_spec.rb
  - rvm: default
    env: LINT=metadata
    dist: trusty
    bundler_args:
    script: rake metadata_lint
  - rvm: default
    env: LINT=puppetcode
    dist: trusty
    bundler_args:
    script: rake lint
  - rvm: default
    env: JOB=validate
    dist: trusty
    bundler_args:
    script: rake validate
  - rvm: default
    env: JOB=rspec
    dist: trusty
    bundler_args:
    script: rake spec
```

```
Started by user anonymous
Building in workspace /home/jprats/NetBeansProjects/traveloci/work/jobs/hari/workspace
[workspace] $ /bin/bash -c "rake lint"
[workspace] $ /bin/bash -c "rake validate"
 == JOB 0 ==
 Command :/bin/bash -c "rake lint"
 return code: 1
 Job status: FAILED

 == JOB 1 ==
 Command :/bin/bash -c "rake validate"
 return code: 0
 Job status: SUCCESS

 == JOB 0 ==
 OUTPUT: 

rake aborted!


Tasks: TOP => lint
(See full trace by running task with --trace)
manifests/concatfile.pp - ERROR: hari::concatfile not in autoload module layout on line 1
manifests/concatfile_fragment.pp - ERROR: hari::concatfile_fragment not in autoload module layout on line 1
manifests/file.pp - ERROR: hari::file not in autoload module layout on line 1
manifests/init.pp - ERROR: hari not in autoload module layout on line 1


 == JOB 1 ==
 OUTPUT: 

puppet parser validate --noop manifests/file.pp
puppet parser validate --noop manifests/concatfile_fragment.pp
puppet parser validate --noop manifests/concatfile.pp
puppet parser validate --noop manifests/init.pp
ruby -c spec/acceptance/standard_spec.rb
Syntax OK
ruby -c spec/spec_helper_acceptance.rb
Syntax OK
ruby -c spec/classes/init_spec.rb
Syntax OK
ruby -c spec/spec_helper.rb
Syntax OK


Build step 'execute traveloci task' marked build as failure
Finished: FAILURE
```
