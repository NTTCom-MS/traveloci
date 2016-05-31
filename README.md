# traveloci

## build

```
# mvn -DskipTests package
````

## install

## usage

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
 == JOB 0 ==
env:BEAKER_set="centos6-docker"
script:sudo service docker restart ; sleep 10 && /opt/beaker/bin/rspec spec/acceptance/*_spec.rb
[workspace] $ /bin/bash -c "sudo service docker restart ; sleep 10 && /opt/beaker/bin/rspec spec/acceptance/*_spec.rb"
docker stop/waiting
docker start/running, process 13472
/opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec/helpers/serverspec.rb:43: warning: already initialized constant Module::VALID_OPTIONS_KEYS
/opt/beaker/lib/ruby/gems/2.1.0/gems/specinfra-2.44.1/lib/specinfra/configuration.rb:4: warning: previous definition of VALID_OPTIONS_KEYS was here
/opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-2.26.0/lib/beaker/options/hosts_file_parser.rb:23:in `parse_hosts_file': Host file '/home/jprats/NetBeansProjects/traveloci/work/jobs/hari/workspace/spec/acceptance/nodesets/"centos6-docker".yml' does not exist! (ArgumentError)
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-2.26.0/lib/beaker/options/parser.rb:195:in `parse_args'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec/beaker_shim.rb:58:in `setup'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec/spec_helper.rb:45:in `block in <top (required)>'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core.rb:97:in `configure'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec/spec_helper.rb:5:in `<top (required)>'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:55:in `require'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:55:in `require'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec.rb:5:in `<module:BeakerRSpec>'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/beaker-rspec-5.3.0/lib/beaker-rspec.rb:1:in `<top (required)>'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:135:in `require'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:135:in `rescue in require'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:144:in `require'
	from /home/jprats/NetBeansProjects/traveloci/work/jobs/hari/workspace/spec/spec_helper_acceptance.rb:1:in `<top (required)>'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:55:in `require'
	from /opt/beaker/lib/ruby/2.1.0/rubygems/core_ext/kernel_require.rb:55:in `require'
	from /home/jprats/NetBeansProjects/traveloci/work/jobs/hari/workspace/spec/acceptance/standard_spec.rb:1:in `<top (required)>'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/configuration.rb:1327:in `load'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/configuration.rb:1327:in `block in load_spec_files'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/configuration.rb:1325:in `each'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/configuration.rb:1325:in `load_spec_files'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/runner.rb:102:in `setup'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/runner.rb:88:in `run'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/runner.rb:73:in `run'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/lib/rspec/core/runner.rb:41:in `invoke'
	from /opt/beaker/lib/ruby/gems/2.1.0/gems/rspec-core-3.3.2/exe/rspec:4:in `<top (required)>'
	from /opt/beaker/bin/rspec:23:in `load'
	from /opt/beaker/bin/rspec:23:in `<main>'
Total time spent: 11743 ms

 == JOB 1 ==
env:LINT=metadata
script:rake metadata_lint
[workspace] $ /bin/bash -c "rake metadata_lint"
Total time spent: 301 ms

 == JOB 2 ==
env:LINT=puppetcode
script:rake lint
[workspace] $ /bin/bash -c "rake lint"
rake aborted!


Tasks: TOP => lint
(See full trace by running task with --trace)
manifests/concatfile.pp - ERROR: hari::concatfile not in autoload module layout on line 1
manifests/concatfile_fragment.pp - ERROR: hari::concatfile_fragment not in autoload module layout on line 1
manifests/file.pp - ERROR: hari::file not in autoload module layout on line 1
manifests/init.pp - ERROR: hari not in autoload module layout on line 1
Total time spent: 289 ms

 == JOB 3 ==
env:JOB=validate
script:rake validate
[workspace] $ /bin/bash -c "rake validate"
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
Total time spent: 3803 ms

 == JOB 4 ==
env:JOB=rspec
script:rake spec
[workspace] $ /bin/bash -c "rake spec"
/usr/bin/ruby1.9.1 -S rspec spec/classes/init_spec.rb --color
F

Failures:

  1) hari with defaults for all parameters
     Failure/Error: it { should contain_class('hari') }
     Puppet::Error:
       Could not find class hari for croscat.atlasit.local on node croscat.atlasit.local
     # ./spec/classes/init_spec.rb:13:in `block (3 levels) in <top (required)>'

Finished in 0.15132 seconds
1 example, 1 failure

Failed examples:

rspec ./spec/classes/init_spec.rb:13 # hari with defaults for all parameters /usr/bin/ruby1.9.1 -S rspec spec/classes/init_spec.rb --color failed
Total time spent: 1393 ms

Build step 'execute traveloci task' marked build as failure
Finished: FAILURE
```
