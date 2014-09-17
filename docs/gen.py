#!/usr/bin/env python

import json
import os
import sys

import gflags
import jinja2

FLAGS = gflags.FLAGS

gflags.DEFINE_string('api_docs_path', '', 'The directory with all the '
    'api-docs, including service.json')
gflags.DEFINE_string('md_out', '', 'The output file for Markdown docs')
gflags.DEFINE_string('html_out', '', 'The output file for HTML docs')

_template_loader = jinja2.FileSystemLoader(os.path.dirname(__file__))
_env = jinja2.Environment(loader=_template_loader)

def main(args):
  service = None
  with open(os.path.join(FLAGS.api_docs_path, 'service.json')) as f:
    service = json.loads(f.read())

  apis = []
  models = []
  for api in service['apis']:
    p = api['path'].replace('{format}', 'json')[1:]
    with open(os.path.join(FLAGS.api_docs_path, p)) as f:
      api = json.loads(f.read())
      if 'models' in api:
        models.append(api.pop('models'))
      apis.append(api)

  service.pop('apis')
  service['apiDocuments'] = apis
  service['models'] = models

  tpl = _env.get_template('template.md')
  docs = tpl.render(**service)

  out = FLAGS.md_out or os.path.join(os.path.dirname(__file__), 'docs.md')
  with open(out, 'w') as f:
    f.write(docs)

  out = FLAGS.html_out
  if out:
    with open(out, 'w') as f:
      f.write('Not implemented yet')


if __name__ == '__main__':
  FLAGS(sys.argv)
  main(sys.argv[1:])
