'use strict'

const webpack = require('webpack')
const { merge }  = require('webpack-merge')
let makeWebpackConfig = require('./makeConfig')

module.exports = merge(makeWebpackConfig, {
    plugins : [
        new webpack.DefinePlugin({
            'process.env.NODE_ENV' : JSON.stringify('production')
        })
    ]
})