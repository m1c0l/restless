import urllib,urllib2
url='http://159.203.243.194/api/update/user/1'
values = {'first_name' : 'Roll'}
data = urllib.urlencode(values)
req = urllib2.Request(url,data)
print urllib2.urlopen(req).read()