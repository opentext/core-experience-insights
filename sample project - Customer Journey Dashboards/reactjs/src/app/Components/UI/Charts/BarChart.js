import { useD3 } from 'AppUtils/hooks/useD3'
import React from 'react'
import * as d3 from 'd3'
import PropTypes from 'prop-types'
import { COLORS } from 'App/constants'
import { numberFormatter } from 'AppUtils'

const BarChart = ({ data, categories, values }) => {
    const height = 600
    const width = 600
    const DURATION = 1000

    const ref = useD3(
        
        (svg) => {
            const margin = { top: 20, right: 30, bottom: 30, left: 60 }
          
            var tooltip = d3
        .select('body')
        .append('div')
        .attr('class', 'otxec-d3-toolTip')
            const y = d3
        .scaleBand()
        .domain(data.map((d) => d[categories]))
        .rangeRound([margin.left, width - margin.right])
        .padding(0.2)

            const x = d3
        .scaleLinear()
        .domain([0, d3.max(data, (d) => d[values])])
        .rangeRound([height , margin.top])

            const color = d3
        .scaleOrdinal()
        .domain(data.map((d) => d[categories]))
        .range(COLORS)

            const onMouseOverHighlight = function () {
               
                d3.select(this).attr('class', 'highlight')
                d3.select(this)
          .transition()
          .duration(DURATION)
          .style('opacity', .8)
          .attr('height', y.bandwidth() + 5)
          .attr('x', 0)
          .attr('width', (d) => height- x(d[values])+ 10)
            }

            const onMouseOut = function () {
               
                tooltip.style('display', 'none') //hiding tooltip also
                d3.select(this).classed('highlight', false).classed('bar', true)
                d3.select(this)
          .transition()
          .duration(DURATION)
          .style('opacity', 1)
          .attr('height', y.bandwidth())
          .attr('x', 10)
          .attr('width',  (d) => height- x(d[values]))
            }
            
            const yAxis = d3
            .axisLeft(y)
            .ticks(5)
            .ticks(null, 's')
            .tickFormat((d) => d)
            svg
            .select('.y-axis')
            .attr('dy', '1em')
           .attr('transform', `translate(${width- 2* margin},0)`)
           .call(yAxis)
           
            svg
        .select('.plot-area')
        .selectAll('.bar')
        .data(data)
        .join('rect')
        .attr('y', (d) => y(d[categories]))
        .attr('width', 10)
        .attr('fill', (d) => color(d[categories]))
        .attr('x',10)
        .attr('height', y.bandwidth())
        .attr('class', 'bar')
        .on('mouseover', onMouseOverHighlight)
        .on('mousemove', (event, d) => {
            tooltip
            .style('left', event.pageX + 20 + 'px')
            .style('top', event.pageY + 10 + 'px')
            .style('display', 'inline-block')
            .html(d[categories] + '<br>' + d[values])
        })
        .on('mouseout', onMouseOut)
            svg
        .selectAll('.bar')
        .transition()
        .ease(d3.easeLinear)
        .duration(DURATION)
        .attr('x', 10)
        .attr('height', y.bandwidth())
        .attr('y', (d) => y(d[categories]))
        .attr('width', (d) => height- x(d[values]))
        .delay((d, i) => i * 50)

            svg
        .selectAll('.label-text')
        .data(data)
        .join('text')
        .text((d) => numberFormatter(d[values]))
        .attr('class', 'label-text')
        .attr('x', 10)
        .attr('y', (d) => {
            return y(d[categories]) + y.bandwidth() / 2 - 5
        })
        .transition()
        .ease(d3.easeLinear)
        .duration(DURATION)
        .attr('y', (d) => {
            return y(d[categories]) + y.bandwidth() / 2 - 5
        })
        .attr('x', (d) => height- x(d[values]) +15)
        },
        [data]
    )
    return (
        <svg
            ref={ref}
            viewBox={`0 0  ${width} ${height}`}
            style={{
                height      : '60vh',
                width       : '100%',
                marginRight : '0px',
                marginLeft  : '0px',
                overflow    : 'visible',
            }}
        >
            <g className="plot-area" />
            <g className="x-axis otxec-d3-axis-labels" />
            <g className="y-axis otxec-d3-axis-labels" />
        </svg>
    )
}

BarChart.propTypes = {
    data       : PropTypes.array,
    categories : PropTypes.string,
    values     : PropTypes.string,
}

export default BarChart
