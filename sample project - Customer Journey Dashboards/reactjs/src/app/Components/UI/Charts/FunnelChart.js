import React, {useEffect,useRef} from 'react'
import D3Funnel from 'd3-funnel'
import nextId from 'react-id-generator'
import PropTypes from 'prop-types'
D3Funnel.defaults.block.dynamicHeight = false

import {COLORS} from 'App/constants'
import { numberFormatter } from 'AppUtils'

const FunnelChart = ({ data, categories, values }) => {

    data = data.map((row) => [row[categories], row[values]])
    let options = {
        block : {
            dynamicHeight : false,
            minHeight     : 15,
            fill          : {
                scale : COLORS
            }
        },
        chart : {
            bottomWidth : 3 / 8,
            bottomPinch : 1,
            animate     : 400,
            width       : 600
        },
        label : {
            format : function(label,value) {
                return label + '\n' +  numberFormatter(value)
            }
        }
    }
    const wrapperRef = useRef()
    const funnelId = nextId('funnel-id-')
  
    useEffect(() => {
        const chart = new D3Funnel('#'+funnelId)
        chart.draw(data, options)
    }, [data])
    return <div ref={wrapperRef} id={funnelId} className="funnelChart">

    </div>
}
FunnelChart.propTypes = {
    data       : PropTypes.array,
    categories : PropTypes.string,
    values     : PropTypes.string
}
export default FunnelChart
