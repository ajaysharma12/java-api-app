#! /usr/bin/env python

import sys
import os
from wsgiref import simple_server, util

import gflags

import gen

service = None
_styles = os.path.join(os.path.dirname(__file__), 'style.css')

def app(environ, start_response):
  global service
  if not service:
    service = gen.GetService()

  path = util.shift_path_info(environ)
  if path == 'style.css':
    with open(_styles) as f:
      data = f.read()
    start_response('200 OK', [('Content-Type', 'text/css')])
    return [str(data)]

  start_response('200 OK', [('Content-Type', 'text/html')])
  return [str(gen.RenderHtml(service))]


def main(args):
  httpd = simple_server.make_server('', 8000, app)
  print "Serving HTTP on port 8000..."
  httpd.serve_forever()

if __name__ == '__main__':
  gflags.FLAGS(sys.argv)
  main(sys.argv[1:])
