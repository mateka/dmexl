import os
import csv
import codecs
from subprocess import check_output

memory='6048M'
data_path = 'data'
log_path="log.csv"

def invoke(processors, method, iterations, particles, table):
    proc_list = '1-' + str(processors)
    command = ['taskset', '--cpu-list', proc_list, 'java', '-Xmx', memory,
               '-Xms', memory, 'dmexlib_rseslib-0.1-SNAPSHOT.jar',
               'PSOReductsExperiments', processors, method,
               iterations, particles, table]

    result = ''
    try:
        result = check_output(command)
    except Exception:
        pass

    flog = codecs.open(log_path, 'a', encoding='utf-8')
    log = csv.writer(flog)
    log.writerow([method, processors, table, iterations, particles] + result.split('\t'))
    flog.close()


def proc_files(processors, method, iterations, particles):
    for _, _, files in os.walk(data_path):
        for fname in files:
            path = os.path.join(data_path, fname)
            invoke(processors, method, iterations, particles, path)

def run_experiments(processors, method):
    for particles in range(10, 101, 10):
        for iterations in range(25, 151, 25):
            proc_files(processors, method, iterations, particles)

run_experiments(1, 'seq')

for processors in [2, 4, 8, 12]:
    run_experiments(processors, 'task')