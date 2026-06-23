#!/bin/sh

PASSWORD="$1"

NODE_PATH="$(npm root -g)" node -e 'const zxcvbn = require("zxcvbn"); console.log(zxcvbn(process.argv[1] || "").score);' "$PASSWORD"
