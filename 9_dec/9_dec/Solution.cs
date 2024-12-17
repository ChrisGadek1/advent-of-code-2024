using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _9_dec
{
    internal class Solution
    {
        public void movePartitions(Disk disk)
        {
            int occupiedNumber = disk.NumberOfLastOccupiedPartition() , emptyNumber = 1;
            while(occupiedNumber >= emptyNumber)
            {
                DiskPartition movingPartition = disk.Partitions[occupiedNumber];
                DiskPartition emptyPartition = disk.Partitions[emptyNumber];
                DiskPartition newPartition = disk.addPartition(new DiskPartition(PartitionType.OCCUPIED, movingPartition.Id, 0), emptyNumber);
                occupiedNumber++; emptyNumber++;
                int offsetSize = Math.Min(movingPartition.Size, emptyPartition.Size);
                newPartition.Size += offsetSize;
                emptyPartition.Size -= offsetSize;
                movingPartition.Size -= offsetSize;
                if(emptyPartition.Size == 0)
                {
                    emptyNumber += 2;
                }
                if(movingPartition.Size == 0)
                {
                    occupiedNumber -= 2;
                }
            }
            disk.RemoveAllParitionsWithNoSize();
        }

        public void movePartitionsWithoutFragmentation(Disk disk)
        {
            int emptyNumber = 1;
            while(emptyNumber < disk.Partitions.Count)
            {
                if(disk.Partitions[emptyNumber].PartitionType == PartitionType.EMPTY && disk.Partitions[emptyNumber].Size > 0)
                {
                    disk.moveRightestPartitionsToEmptySpace(emptyNumber);
                }
                emptyNumber++;
            }
            disk.RemoveAllParitionsWithNoSize();
        }

        static void Main(string[] args)
        {
            string pathToFile = "input.txt";
            Disk disk = Disk.loadFromFile(pathToFile);
            Solution solution = new Solution();
            solution.movePartitions(disk);
            Console.WriteLine("part one: "+disk.CalculateChecksum());

            disk = Disk.loadFromFile(pathToFile);
            solution.movePartitionsWithoutFragmentation(disk);
            Console.WriteLine("part two: " + disk.CalculateChecksum());
        }
    }
}
