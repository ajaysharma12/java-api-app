#!/usr/bin/env python

import sys
import httplib
import urllib

redirect_url = 'chrome:extension://dfdbebfbdka92ndkn0n2g/token.html?'

conn = httplib.HTTPConnection('localhost:8080')

params = urllib.urlencode({'u': 'arunjit', 'p': 'pass'})
headers = {"Content-type": "application/x-www-form-urlencoded"}
conn.request('POST', '/api/auth?redirect_url=boo', params, headers)
resp = conn.getresponse()
if resp.status not in [200, 201, 204]:
  print resp.read()
  sys.exit(1)

jwt_token = resp.getheader('Location')[4:]
print jwt_token

headers = {'Authorization': jwt_token}

def Get(url):
  conn.request('GET', url, '', headers)
  resp = conn.getresponse()
  print '\n-----\n'
  print 'GET %s [%s %s]' % (url, resp.status, resp.reason)
  print resp.read()

Get('/api/-/let-me-in')
Get('/api/-/sc')

conn.request('DELETE', '/api/auth', '', headers)
resp = conn.getresponse()
print '\n-----\n'
print 'DELETE /api/auth [%s %s]' % (resp.status, resp.reason)
print resp.read()

conn.close()
