'use strict'

const webpack = require('webpack')
const { merge } = require('webpack-merge')
let makeWebpackConfig = require('./makeConfig')
const path = require('path')

module.exports = merge(makeWebpackConfig, {
    mode      : 'development',
    devtool   : 'inline-source-map',
    devServer : {
        host     : process.env.DEV_REST_DOMAIN || 'localhost',
        port     : 5000,
        compress : true,
        static   : {
            directory : path.join(__dirname, '../public'),
        },
        open   : true,
        hot    : true, 
        client : {
            progress : true,
        }, 
        headers : {
            'X-Development-Mode' : 'true',
        },
        watchFiles : {
            options : {
                usePolling : true,
            }
        },
        proxy : {
            '/api/**' : {
                target : process.env.DEV_REST_URL || 'http://localhost:8021'
            },
            '/login/**' : {
                target : process.env.DEV_REST_URL || 'http://localhost:8021'
            }, 
            '/perform_login/**' : {
                target : process.env.DEV_REST_URL || 'http://localhost:8021'
            },
            '/logout/**' : {
                target : process.env.DEV_REST_URL || 'http://localhost:8021'
            }
        }
    },
    plugins : [
        // webpack-dev-server enhancement plugins
        //new DashboardPlugin(),
        new webpack.NoEmitOnErrorsPlugin(),
        // do not emit compiled assets that include errors
        new webpack.DefinePlugin({
            'process.env.NODE_ENV' : JSON.stringify('development')
        })
    ]
})