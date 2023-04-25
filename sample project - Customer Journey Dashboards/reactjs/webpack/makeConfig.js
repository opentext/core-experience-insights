
const dotenv = require('dotenv')
const path = require('path')
const webpack = require('webpack')

const CopyWebpackPlugin = require('copy-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CompressionPlugin = require('compression-webpack-plugin')

dotenv.config()
const exposed = [
    'DEFAULT_LOCALE',
    'DEBUG_MODE',
    'DEV_REST_URL',
    'DEV_REST_DOMAIN'
]
const exposedEnvironment = {}
exposed.forEach(i => {
    exposedEnvironment[i] = JSON.stringify(process.env[i])
})

module.exports = {
    entry : {  
        app : [
            'react-hot-loader/patch',
            'react', // Include this to enforce order
            'react-dom', // Include this to enforce order
            path.resolve(__dirname, '../src/app/index.js')
        ],
        oops : [
            'react-hot-loader/patch',
            'react', // Include this to enforce order
            'react-dom', // Include this to enforce order
            path.resolve(__dirname, '../src/oops/index.js')
        ]
    },
    performance : {
        hints : process.env.NODE_ENV === 'production' ? 'warning' : false
    },
    resolve : {
        extensions : ['*', '.js', '.jsx'],
        alias      : {
            App      : path.resolve(__dirname, '../src/app'),
            AppUI    : path.resolve(__dirname, '../src/app/Components/UI'),
            AppApps  : path.resolve(__dirname, '../src/app/Components/Applications'),
            Oops     : path.resolve(__dirname, '../src/oops'),
            OopsApps : path.resolve(__dirname, '../src/oops/Components/Applications'),
            AppUtils : path.resolve(__dirname, '../src/app/utils'),
        }
    },
    output : {
        filename          : 'js/[name].min.js',
        path              : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static'), 
        sourceMapFilename : '[name].map',
        clean             : true,
        publicPath        : '/'
    },
    module : {
        rules : [
            {
                test    : /\.(js|jsx)$/,
                exclude : /node_modules/,
                use     : {
                    loader  : 'babel-loader',
                    options : {
                        presets : ['@babel/preset-env', '@babel/preset-react']
                    }
                }
            },
            {
                test : /\.css$/i,
                use  : ['style-loader', 'css-loader'],
            },
            {
                test : /\.scss$/,
                use  : [
                    {
                        loader : 'style-loader'
                    },
                    {
                        loader : 'css-loader'
                    },
                    {
                        loader : 'sass-loader'
                    }
                ]
            }
        ]
    },
    plugins : [
        new webpack.DefinePlugin({
            'process.env' : exposedEnvironment
        }),
        new CopyWebpackPlugin({
            patterns : [
                {
                    from : 'public/fonts',
                    to   : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static/fonts')
                },
                {
                    from : 'public/images',
                    to   : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static/images')
                },
                {
                    from : 'public/favicon.ico',
                    to   : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static')
                }
            ],
        }), 
        new HtmlWebpackPlugin({
            chunks   : ['app'],
            filename : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static/index.html'),
            template : path.resolve(__dirname, '../public/index.html'),
            minify   : {
                removeComments                : false,
                collapseWhitespace            : false,
                removeRedundantAttributes     : true,
                useShortDoctype               : true,
                removeEmptyAttributes         : true,
                removeStyleLinkTypeAttributes : true,
                keepClosingSlash              : true,
                minifyJS                      : false,
                minifyCSS                     : true,
                minifyURLs                    : true
            }
        }),
        new HtmlWebpackPlugin({
            chunks   : ['oops'],
            filename : path.resolve(__dirname, '../../spring-boot-ui/src/main/resources/static/error.html'),
            template : path.resolve(__dirname, '../public/error.html'),
            minify   : {
                removeComments                : false,
                collapseWhitespace            : false,
                removeRedundantAttributes     : true,
                useShortDoctype               : true,
                removeEmptyAttributes         : true,
                removeStyleLinkTypeAttributes : true,
                keepClosingSlash              : true,
                minifyJS                      : false,
                minifyCSS                     : true,
                minifyURLs                    : true
            }
        }),
        new MiniCssExtractPlugin({
            // Options similar to the same options in webpackOptions.output
            // both options are optional
            filename      : '[name].css',
            chunkFilename : '[id].css'
        }),
        new webpack.ProvidePlugin({
            React    : 'react',
            ReactDOM : 'react-dom'
        }),
        new CompressionPlugin({
            test      : /\.(js|css|html|svg|woff|woff2)$/,
            asset     : '[path].gz[query]',
            algorithm : 'gzip',
            minRatio  : 1024
        })
    ]
}