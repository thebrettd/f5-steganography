from django.http import HttpResponse

import requests

import os

base_nyt_url = 'http://api.nytimes.com'

# Create your views here.
def index(request):
    return HttpResponse('Hello!')

def http200():
    response = HttpResponse()
    response.status_code = 200
    return response


def subscribe(request):
    print request
    results = retrieve_most_popular()
    yo_user_with_link(results, 'thebrettd')
    #Send a yo to the user with the popular article
    return http200()


def retrieve_most_popular(resource_type='mostviewed', sections='all-sections', interval=30,
                          api_key='d70b35e4b63397f6236fcf4746a22e85:16:57738177'):

    request_string = '%s/svc/mostpopular/v2/%s/%s/%s/?api-key=%s' % (base_nyt_url, resource_type, sections, interval, api_key)

    requests_get = requests.get(request_string)
    return 'http://www.google.com'


def yoall_with_link(link):
    payload = {'api_token': os.environ.get('YO_API_KEY'), 'link': link}
    requests.post("http://api.justyo.co/yoall/", data=payload)


def yo_user_with_link(link, username):
    payload = {'api_token': os.environ.get('YO_API_KEY'), 'username' : username, 'link': link}
    requests.post("http://api.justyo.co/yo/", data=payload)


def schedule():
    popular_link = retrieve_most_popular()
    yoall_with_link(popular_link)
