/**
  * 快排
  * @see 'https://blog.csdn.net/libaineu2004/article/details/82253412'
  */
def quickSort(left: Int, right: Int, arr: Array[Int]): Unit = {
    // 1). 取数组最最左边为左点(15),取数组最右边为右点(16),取数组中间值为基准点(即数据分为两组的中间值点10)
    // 左边
    var l: Int = left
    // 右边
    var r: Int = right
    // 取中间值
    var pivot = arr((left + right) / 2)
    var temp = 0

    // Array(10, 11, 2, -1, 3)
    breakable {
      // 2). 左点向右走,直到大于中间值(15>10),此时右点向左走,直到小于中间值(8<10),交换左右点的值
      while (l < r) {

        //从左点向右遍历，直到找到比中间值大的
        while (arr(l) < pivot) {
          l += 1
        }

        //从右点向左遍历，直到找到比中间值小的
        while (arr(r) > pivot) {
          r -= 1
        }

        // 3). 左点向右走,直到大于中间值(无), 此时右点向左走,直到小于中间值(无), 第一轮结束
        // 判断是否已经越过中间值
        if (l >= r) {
          break()
        }

		    // 交换数据
        temp = arr(l)
        arr(l) = arr(r)
        arr(r) = temp
      }
    }

    if (l == r) {
      l += 1
      r -= 1
    }

    // 4). 对中间值左右两边两个数组调用自身排序方法(递归), 将左右两个数组排序

    //向左递归
    if (left < r) {
      quickSort(left, r, arr)
    }

    //向右递归
    if (right > l) {
      quickSort(l, right, arr)
    }

}