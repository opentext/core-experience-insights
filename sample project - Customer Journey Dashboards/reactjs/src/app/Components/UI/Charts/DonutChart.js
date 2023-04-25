import { useD3 } from 'AppUtils/hooks/useD3'
import React from 'react'
import * as d3 from 'd3'
import PropTypes from 'prop-types'
import { COLORS} from 'App/constants'
import { numberFormatter } from 'AppUtils'

const DonutChart = ({ data, categories, values }) => {


    let dimensions = {
        width  : 800,
        height : 600,
        margin : 50,
    }
    function getColorByBgColor(bgColor) {
        if (!bgColor) { return '#000' }
        return (parseInt(bgColor.replace('#', ''), 16) > 0xffffff / 2) ? '#000' : '#fff'
    }
    var labelFormatter = function(label,num) {				
        if (label && label.length > 25) {
            label = label.trim(label.substring(0, 22)) + '...'
        }
        return  label + ' (' + numberFormatter(num) + ')'
    }
    const ref = useD3(
        (svg) => {
            d3.selectAll('.otxec-d3-toolTip').remove()
            var total= d3.sum(data, (d)=> {return d[values]})
            var tooltip = d3
        .select('body')
        .append('div')
        .attr('class', 'otxec-d3-toolTip')
            function showToolTip(event, d) {
                tooltip
            .style('left', event.pageX + 10 + 'px')
            .style('top', event.pageY + 20 + 'px')
            .style('display', 'inline-block')
            .html(d.data[categories] + '<br>' + d.data[values])
            }

            var legendSpace = 100

            dimensions.ctrWidth =
        dimensions.width - legendSpace - dimensions.margin * 2
            dimensions.ctrHeight = dimensions.height - dimensions.margin * 2


            const radius = dimensions.ctrWidth / 2

            function arcTween(d) {
                var interpolate = d3.interpolate(this._current, d)
                this._current = interpolate(0)
                return function (t) {
                    return arc(interpolate(t))
                }
            }


            const arcsGroup = svg
        .select('.arcs')
        .attr(
            'transform',
            `translate(${dimensions.width / 2},${dimensions.height / 2})`
        ) // center of the container

            const labelsGroup = svg
        .select('.labels')

        .attr(
            'transform',
            `translate(${dimensions.width / 2},${dimensions.height / 2})`
        ) // center of the container

            // Pie Generator
            const pieGenerator = d3
        .pie()
        .padAngle(0.01)
        .value((d) => d[values])
        .sort(null)
            const slices = pieGenerator(data)
            const arc = d3.arc().outerRadius(radius).innerRadius(radius * 0.5)

            const arcLabels = d3
        .arc()
        .outerRadius(radius + dimensions.margin)
        .innerRadius(radius * 0.5)

            const colorScale = d3
        .scaleOrdinal()
        .domain(data.map((d) => d[categories]))
        .range(COLORS)
        //draw graphs   
            arcsGroup
        .selectAll('path')
        .data(slices)
        .join('path')
        .attr('d', arc)
        .on('click', () => {
            tooltip.style('display', 'none')
        })
        .on('mouseover', (e,d)=>showToolTip(e,d))
        .on('mousemove', (e,d)=>showToolTip(e,d))
        .on('mouseout', () => {

            tooltip.style('display', 'none')
        })
       
        .attr('fill', (d) => colorScale(d.data[categories]))
        .transition()
        .duration(3000)
        .attrTween('d', arcTween)

            labelsGroup.select('.labels text').remove()
            d3.selectAll('.lable-text').remove()

            labelsGroup
        .selectAll('labels')
        .data(slices)
        .join('text')
        .classed('lable-text', true)

        .attr('transform', (d) => `translate(${arcLabels.centroid(d)})`)
        .call((text) =>
            text
            .filter((d) => d.endAngle - d.startAngle > 0.25)
            .append('tspan')
            .attr('class', 'otxec-d3-labels')
            .style('fill', (d) => getColorByBgColor(colorScale(d.data[categories])))
            .attr('y', -5)
            .attr('text-anchor', 'middle')
            .classed('lable-text', true)
            .text((d) => d.data[categories])
        )
        .call((text) =>
            text
            .filter((d) => d.endAngle - d.startAngle > 0.25)
            .append('tspan')
            .style('fill', (d) => getColorByBgColor(colorScale(d.data[categories])))
            .attr('y', 16)
            .attr('x', 0)
            .attr('class', 'otxec-d3-labels')
            .classed('lable-text', true)
            .text((d) => Math.round(d.data[values]*100/total) + '%')
            .attr('text-anchor', 'middle')
        )
        .transition()
        .delay(6000)
            const legendGroup = svg
        .select('.legends')

            legendGroup.selectAll('.otxec-d3-legends').remove()
            var legendG = legendGroup.selectAll('.legends  otxec-d3-legends')
        .data(data)
        .join('g')
        .attr('transform', (d, i) => {

            return 'translate(' + (-dimensions.margin) + ',' + (radius + i * 20 + 20) + ')'
        })
        .attr('class', 'otxec-d3-legends')

            legendG.append('rect')
        .attr('width', 12)
        .attr('height', 12)
        .attr('fill', (d) => colorScale(d[categories]))

            legendG.append('text')
        .text((d) => labelFormatter(d[categories],d[values]))
        .attr('y', 11)
        .attr('x', 14)
        }, [data, categories, values, dimensions])

    return (
        <svg
            ref={ref}
            viewBox={`0 0  ${dimensions.width} ${dimensions.height}`}
            style={{
                height            : '60vh',
                width             : '70vw',
                marginRight       : '5px',
                marginLeft        : '5px',
                overflow          : 'visible',
                alignmentBaseline : 'baseline',
                verticalAlign     : 'top'

            }}
        >
            <g className="plot-area" />
            <g className="arcs" />
            <g className="labels" />
            <g className="legends" />
        </svg>
    )
}
DonutChart.propTypes = {
    data       : PropTypes.array,
    categories : PropTypes.string,
    values     : PropTypes.string
}
export default DonutChart
