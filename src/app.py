# coding: utf-8
"""A server for test"""

from flask import Flask, jsonify
import os

app = Flask(__name__)

app.config['SECRET_KEY'] = os.urandom(24)


@app.route('/user', methods=['POST', 'GET'])
def user():
    return jsonify({"message": "ok", "data": {'name': 'newt'}})

@app.route('/users', methods=['POST', 'GET'])
def users():
    return jsonify({"message": "success", "data": [{'name': 'newt'}, {'name': 'teo'}]})

if __name__ == "__main__":
    app.run("0.0.0.0", 8080)