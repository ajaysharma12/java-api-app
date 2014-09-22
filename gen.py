#!/usr/bin/env python

import json
import os
import re
import sys

import gflags
import jinja2

FLAGS = gflags.FLAGS

gflags.DEFINE_string('api_docs_path', '', 'The directory with all the '
    'api-docs, including service.json')
gflags.DEFINE_string('html_out', '', 'The output file')
gflags.DEFINE_boolean('show', False, 'Show the generated service')

_template_loader = jinja2.FileSystemLoader(os.path.dirname(__file__))
_env = jinja2.Environment(loader=_template_loader)


def ItemsRef(value):
  if 'items' in value:
    return Ref(value['items'])
  return ''


def Ref(value):
  if '$ref' in value:
    return value['$ref']
  return ''


def ToUnderscores(value):
  return re.sub(r'([A-Z])', r'_\1', value).lower()


_env.filters['itemsref'] = ItemsRef
_env.filters['ref'] = Ref
_env.filters['to_underscores'] = ToUnderscores


def FixOperation(op):
  query_params = []
  body_params = []
  extra_params = []
  for param in op['parameters']:
    if param['paramType'] == 'query':
      query_params.append(param)
    elif param['paramType'] == 'body':
      body_params.append(param)
    else:
      extra_params.append(param)
  del op['parameters']
  if query_params:
    op['queryParameters'] = query_params
  if body_params:
    op['requestBody'] = body_params[0]
  if extra_params:
    op['parameters'] = extra_params
  return op


def FixApi(api):
  for op in api['operations']:
    FixOperation(op)
  return api


def FixApiDoc(api_doc):
  api_doc['apis'] = sorted(api_doc['apis'], key=lambda x: x['path'])
  for api in api_doc['apis']:
    FixApi(api)
  return api_doc


def FixModels(models):
  ret = {}
  for model in models:
    for t, d in model.iteritems():
      ret[t] = d
  return ret


def FlattenApiDocs(api_docs):
  apis = []
  for api_doc in api_docs:
    for api in api_doc['apis']:
      apis.append(api)
  return (api_docs[0]['basePath'], api_docs[0]['apiVersion'], apis)


def GetService(api_docs_path=None):
  api_docs_path = api_docs_path or FLAGS.api_docs_path
  service = None
  with open(os.path.join(api_docs_path, 'service.json')) as f:
    service = json.loads(f.read())

  apis = []
  models = []
  for api in service['apis']:
    p = api['path'].replace('{format}', 'json')[1:]
    with open(os.path.join(api_docs_path, p)) as f:
      api = json.loads(f.read())
      if 'models' in api:
        models.append(api.pop('models'))
      apis.append(FixApiDoc(api))

  service.pop('apis')
  #service['apiDocuments'] = apis
  service['models'] = FixModels(models)
  service['basePath'], service['apiVersion'], service['apis'] = FlattenApiDocs(apis)
  return service


def RenderHtml(service):
  tpl = _env.get_template('template.html')
  html = tpl.render(**service)
  html = ' '.join(html.split())
  return html


def main(args):
  service = GetService(FLAGS.api_docs_path)

  if FLAGS.show:
    print(json.dumps(service))

  docs = RenderHtml(service)

  out = FLAGS.html_out or os.path.join(os.path.dirname(__file__), 'docs.html')
  with open(out, 'w') as f:
    f.write(docs)


if __name__ == '__main__':
  FLAGS(sys.argv)
  main(sys.argv[1:])
