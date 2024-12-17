using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _9_dec
{
    internal class Disk
    {
        private List<DiskPartition> _partitions;

        public List<DiskPartition> Partitions { get { return _partitions; } }

        public Disk(List<DiskPartition> partitions)
        {
            _partitions = partitions;
        }

        public static Disk loadFromFile(string path)
        {
            string contents = File.ReadAllText(path);
            List<DiskPartition> createdPartirions = new List<DiskPartition>();
            for (int i = 0; i < contents.Length; i++)
            {
                PartitionType partitionType = i % 2 == 0 ? PartitionType.OCCUPIED : PartitionType.EMPTY;
                int id = i / 2;
                DiskPartition diskPartition = new DiskPartition(partitionType, id, contents[i] -  48);
                createdPartirions.Add(diskPartition);
            }
            return new Disk(createdPartirions);
        }

        public int NumberOfLastOccupiedPartition()
        {
            if (_partitions.Last().PartitionType == PartitionType.OCCUPIED)
            {
                return _partitions.Count - 1;
            }
            else
            {
                return _partitions.Count - 2;
            }
        }

        public DiskPartition addPartition(DiskPartition diskPartition, int number)
        {
            _partitions.Insert(number, diskPartition);
            return diskPartition;
        }

        public void RemoveAllParitionsWithNoSize()
        {
            _partitions = _partitions.Where(partition => partition.Size > 0).ToList();
        }

        public long CalculateChecksum()
        {
            long currentMultiplier = 0;
            long currentResult = 0;
            foreach (var partition in _partitions)
            {
                int initialSize = partition.Size;
                while (initialSize > 0) {
                    if (partition.PartitionType == PartitionType.OCCUPIED) 
                    {
                        currentResult += partition.Id * currentMultiplier;
                    }
                    
                    currentMultiplier++;
                    initialSize--;
                }
            }
            return currentResult;
        }

        public void moveRightestPartitionsToEmptySpace(int emptySpaceNumber)
        {
            int lastPartitionNumber = this.NumberOfLastOccupiedPartition();
            while (lastPartitionNumber > emptySpaceNumber)
            {
                if (!_partitions[lastPartitionNumber].IsEmpty() && _partitions[lastPartitionNumber].Size <= _partitions[emptySpaceNumber].Size && _partitions[emptySpaceNumber].Size > 0 && _partitions[lastPartitionNumber].PartitionType == PartitionType.OCCUPIED)
                {
                    _partitions.Insert(emptySpaceNumber, new DiskPartition(PartitionType.OCCUPIED, _partitions[lastPartitionNumber].Id, _partitions[lastPartitionNumber].Size));

                    lastPartitionNumber++;
                    emptySpaceNumber++;
                    _partitions[emptySpaceNumber].Size -= _partitions[lastPartitionNumber].Size;
                    _partitions[lastPartitionNumber].PartitionType = PartitionType.EMPTY;
                }
                lastPartitionNumber--;
            }

        }
    }
}
