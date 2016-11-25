import urllib,urllib2

def test1():
    url='http://159.203.243.194/api/update/user/1'
    values = {'first_name' : 'Roll'}
    data = urllib.urlencode(values)
    req = urllib2.Request(url,data)
    print urllib2.urlopen(req).read()

def test2():
    url='http://159.203.243.194/api/new_user/'
    values = {
        'username' : 'v1nc3nt',
        'password' : 'v1nc3nt11',
        }
    data = urllib.urlencode(values)
    req = urllib2.Request(url,data)
    print urllib2.urlopen(req).read()

test2()