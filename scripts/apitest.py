#!/usr/bin/env python

import httplib
import json
import urllib

conn = httplib.HTTPConnection('localhost:8080')

params = urllib.urlencode({'u': 'arunjit', 'p': 'pass'})
headers = {"Content-type": "application/x-www-form-urlencoded"}
conn.request('POST', '/api/auth', params, headers)
resp = conn.getresponse()
data = resp.read()
print data
data = data[data.index('\n'):]
data = json.loads(data)

api_key = data['api_key']
jwt_token = data['jwt_token']

headers = {'API-Key': api_key, 'Authorization': jwt_token}

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
