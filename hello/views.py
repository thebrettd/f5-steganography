from django.shortcuts import render
from django.http import HttpResponse

from .models import Greeting

import requests

import os

base_nyt_url = 'http://api.nytimes.com'

# Create your views here.
def index(request):
    api_key = os.environ.get('YO_API_KEY')
    return HttpResponse('Hello! ' + api_key)


def db(request):

    greeting = Greeting()
    greeting.save()

    greetings = Greeting.objects.all()

    return render(request, 'db.html', {'greetings': greetings})


def subscribe(request):
    add_user_to_db()
    results = retrieve_most_popular()
    print results.json()
    #Send a yo to the user with the popular article
    return 200

def retrieve_most_popular(resource_type='mostviewed', sections='all-sections', interval=30,
                          api_key='d70b35e4b63397f6236fcf4746a22e85:16:57738177'):

    request_string = '%s/svc/mostpopular/v2/%s/%s/%s/?api-key=%s' % (base_nyt_url, resource_type, sections, interval, api_key)

    return requests.get(request_string)


def add_user_to_db():
    pass