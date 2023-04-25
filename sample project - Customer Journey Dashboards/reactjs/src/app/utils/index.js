/**
 * This function formates the long number into thousands and million 
 * @param {*} num 
 * @returns formatted number into Thousands(K) or Millions (M) 
 */
export const numberFormatter = function(num) {
    if (num >= 1000000) {
        return (num / 1000000).toFixed(1).replace(/\.0$/, '') + 'M'
    }
    if (num >= 1000) {
        return (num / 1000).toFixed(1).replace(/\.0$/, '') + 'K'
    }
    return num
}