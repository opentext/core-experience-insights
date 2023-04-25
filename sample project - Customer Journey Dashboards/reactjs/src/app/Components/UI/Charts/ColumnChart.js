import { useD3 } from 'AppUtils/hooks/useD3'
import React from 'react'
import * as d3 from 'd3'
import PropTypes from 'prop-types'
import { COLORS } from 'App/constants'
import { numberFormatter } from 'AppUtils'

const ColumnChart = ({ data, categories, values }) => {
    const height = 500
    const width = 600
    const margin = { top: 50, right: 20, bottom: 100, left: 20 }
    const DURATION = 1000


    const ref = useD3(
        (svg) => {
            function truncateLabel(text) {
                text.each(function() {
                    let category = d3.select(this).text()
                    if (category.length > 8) {
                        category = category.slice(0,8) + '...'
                        d3.select(this).on('mouseover', (event, d) => {                            
                            tooltip
                            .style('left', event.pageX + 10 + 'px')
                            .style('top', event.pageY + 10 + 'px')
                            .style('display', 'inline-block')
                            .html(d)
                        }).on('mouseout', ()=> {tooltip.style('display', 'none')})
                    }
                    d3.select(this).text(category)
                })
            }

            d3.selectAll('.otxec-d3-toolTip').remove()
            var tooltip = d3
        .select('body')
        .append('div')
        .attr('class', 'otxec-d3-toolTip')
            const x = d3
        .scaleBand()
        .domain(data.map((d) => d[categories]))
        .rangeRound([margin.left, width - margin.right])
        .padding(0.2)

            const y = d3
        .scaleLinear()
        .domain([0, d3.max(data, (d) => d[values])])
        .rangeRound([height - margin.bottom, margin.top])

            const color = d3
        .scaleOrdinal()
        .domain(data.map((d) => d[categories]))
        .range(COLORS)

            const onMouseOut = function () {
                tooltip.style('display', 'none')
                d3.select(this).classed('highlight', false).classed('bar', true)
                d3.select(this)
          .transition()
          .duration(DURATION)
          .attr('width', x.bandwidth())
          .attr('y', (d) => y(d[values]))
          .attr('height', (d) => height - margin.bottom - y(d[values]))
            }

            const xAxis = (g) =>
                g
          .attr('transform', `translate(0,${height - margin.bottom})`)
          .call(d3.axisBottom(x))

            svg.select('.x-axis').call(xAxis)
            svg
        .select('.plot-area')
        .selectAll('.bar')
        .data(data)
        .join('rect')
        .attr('x', (d) => x(d[categories]))
        .attr('width', x.bandwidth())
        .attr('fill', (d) => color(d[categories]))
        .attr('y', height - margin.bottom)
        .attr('height', 0)
        .attr('class', 'bar')
        .on('click', () => {
            tooltip.style('display', 'none')
        })
        .on('mousemove', (event, d) => {
            tooltip
            .style('left', event.pageX + 10 + 'px')
            .style('top', event.pageY + 20 + 'px')
            .style('display', 'inline-block')
            .html(d[categories] + '<br>' + d[values])
        })
        .on('mouseout', onMouseOut)
            svg
        .selectAll('.bar')
        .transition()
        .ease(d3.easeLinear)
        .duration(DURATION)
        .attr('y', (d) => y(d[values]))
        .attr('height', (d) => y(0) - y(d[values]))
        .delay((d, i) => i * 50)

            svg
        .selectAll('.label-text')
        .data(data)
        .join('text')
        .text((d) => numberFormatter(d[values]))
        .attr('class', 'label-text')
        .attr('y', () => {
            return height
        })
        .attr('x', (d) => {
            return x(d[categories]) + x.bandwidth() / 2 - 5
        })
        .transition()
        .ease(d3.easeLinear)
        .duration(DURATION)
        .attr('x', (d) => {
            return x(d[categories]) + x.bandwidth() / 2 - 5
        })
        .attr('y', (d) => {
            return y(d[values]) - 15
        })

            if (data.length > 6) {
                svg
              .selectAll('.tick text')
              .style('text-anchor', 'end')
              .attr('dx', '-.8em')
              .attr('dy', '.15em')
              .attr('transform', 'rotate(-65)')  
            }
            svg
            .selectAll('.tick text')
            .call(truncateLabel)
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
                marginTop   : '5px',
                overflow    : 'visible',
            }}
        >
            <g className='plot-area' />
            <g className='x-axis otxec-d3-axis-labels' />
            <g className='y-axis otxec-d3-axis-labels' />
        </svg>
    )
}

ColumnChart.propTypes = {
    data       : PropTypes.array,
    categories : PropTypes.string,
    values     : PropTypes.string
}

export default ColumnChart
