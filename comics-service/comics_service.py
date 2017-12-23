from flask import Flask
import json
import requests
import random

app = Flask(__name__)


@app.route('/comics')
def comics():
    rand_num = random.randint(0, 1930)
    comic_uri = requests.get('https://xkcd.com/{}/info.0.json'.format(rand_num)).json().get('img')
    comic_alt = requests.get('https://xkcd.com/{}/info.0.json'.format(rand_num)).json().get('alt')
    comic_title = requests.get('https://xkcd.com/{}/info.0.json'.format(rand_num)).json().get('title')
    return json.dumps({"comic_uri": comic_uri,
                       "comic_alt": comic_alt,
                       "comic_title": comic_title})


def main():
    app.run(port=60002, debug=True)


if __name__ == '__main__':
    main()
