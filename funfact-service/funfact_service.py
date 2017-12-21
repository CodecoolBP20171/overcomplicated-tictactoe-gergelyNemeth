from flask import Flask
import json
import requests

app = Flask(__name__)


@app.route('/funfact')
def funfact():
    funfact_value = requests.get('https://api.chucknorris.io/jokes/random').json().get('value')
    return json.dumps({"funfact": funfact_value})


def main():
    app.run(port=60001, debug=True)


if __name__ == '__main__':
    main()
