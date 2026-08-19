import { useEffect, useMemo, useState } from 'react';
import { CheckCircle2, CircleDot, ClipboardList, Plus, Search, Sparkles, Trash2 } from 'lucide-react';
import { createTodo, deleteTodo, getTodos, updateStatus, updateTodo } from './api';
import { Priority, TodoItem, TodoRequest, TodoStatus } from './types';

const priorities: { value: Priority; label: string }[] = [
  { value: 'LAG', label: 'Låg' },
  { value: 'NORMAL', label: 'Normal' },
  { value: 'HOG', label: 'Hög' },
  { value: 'KRITISK', label: 'Kritisk' }
];

const statuses: { value: TodoStatus; label: string }[] = [
  { value: 'REGISTRERAD', label: 'Registrerad' },
  { value: 'STARTAD', label: 'Startad' },
  { value: 'AVSLUTAD', label: 'Avslutad' }
];

const initialForm: TodoRequest = {
  title: '',
  description: '',
  priority: 'NORMAL',
  status: 'REGISTRERAD'
};

function statusClass(status: TodoStatus) {
  return {
    REGISTRERAD: 'bg-blue-100 text-blue-800 ring-blue-200',
    STARTAD: 'bg-orange-100 text-orange-800 ring-orange-200',
    AVSLUTAD: 'bg-green-100 text-green-800 ring-green-200'
  }[status];
}

function priorityClass(priority: Priority) {
  return {
    LAG: 'bg-slate-100 text-slate-700 ring-slate-200',
    NORMAL: 'bg-blue-100 text-blue-800 ring-blue-200',
    HOG: 'bg-orange-100 text-orange-800 ring-orange-200',
    KRITISK: 'bg-red-100 text-red-800 ring-red-200'
  }[priority];
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat('sv-SE', {
    dateStyle: 'short',
    timeStyle: 'short'
  }).format(new Date(value));
}

export default function App() {
  const [todos, setTodos] = useState<TodoItem[]>([]);
  const [form, setForm] = useState<TodoRequest>(initialForm);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<TodoStatus | 'ALLA'>('ALLA');
  const [priorityFilter, setPriorityFilter] = useState<Priority | 'ALLA'>('ALLA');
  const [error, setError] = useState<string | null>(null);

  async function load() {
    try {
      setTodos(await getTodos());
      setError(null);
    } catch {
      setError('Kunde inte hämta uppgifter. Kontrollera att backend kör på port 8080.');
    }
  }

  useEffect(() => {
    load();
  }, []);

  const filteredTodos = useMemo(() => {
    const q = search.trim().toLowerCase();
    return todos
      .filter(todo => statusFilter === 'ALLA' || todo.status === statusFilter)
      .filter(todo => priorityFilter === 'ALLA' || todo.priority === priorityFilter)
      .filter(todo => !q || todo.title.toLowerCase().includes(q) || todo.description.toLowerCase().includes(q))
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  }, [todos, search, statusFilter, priorityFilter]);

  const stats = useMemo(() => ({
    totalt: todos.length,
    startade: todos.filter(t => t.status === 'STARTAD').length,
    avslutade: todos.filter(t => t.status === 'AVSLUTAD').length
  }), [todos]);

  async function saveTodo() {
    if (!form.title.trim() || !form.description.trim()) {
      setError('Rubrik och beskrivning måste fyllas i.');
      return;
    }

    if (editingId) {
      await updateTodo(editingId, form);
    } else {
      await createTodo(form);
    }
    setForm(initialForm);
    setEditingId(null);
    await load();
  }

  function startEdit(todo: TodoItem) {
    setEditingId(todo.id);
    setForm({
      title: todo.title,
      description: todo.description,
      priority: todo.priority,
      status: todo.status
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async function changeStatus(todo: TodoItem, status: TodoStatus) {
    await updateStatus(todo.id, status);
    await load();
  }

  async function remove(id: number) {
    await deleteTodo(id);
    await load();
  }

  return (
    <main className="min-h-screen bg-[radial-gradient(circle_at_top_left,#e0f2fe,transparent_35%),linear-gradient(135deg,#f8fafc,#eef2ff)]">
      <section className="mx-auto max-w-7xl px-5 py-8">
        <header className="mb-8 overflow-hidden rounded-[2rem] bg-combi-ink p-8 text-white shadow-soft relative">
          <div className="absolute inset-y-0 right-0 w-1/2 bg-gradient-to-l from-combi-cyan/30 to-transparent" />
          <div className="relative flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
            <div>
              <div className="mb-3 inline-flex items-center gap-2 rounded-full bg-white/10 px-4 py-2 text-sm text-cyan-100 ring-1 ring-white/15">
                <Sparkles size={16} /> Fullstack-demo med React, Spring Boot och PostgreSQL
              </div>
              <h1 className="text-4xl font-bold tracking-tight lg:text-5xl">Att Göra-lista</h1>
              <p className="mt-3 max-w-2xl text-lg text-slate-200">
                En svensk demoapp där varje statusändring loggas automatiskt och visas direkt under uppgiften.
              </p>
            </div>
            <div className="grid grid-cols-3 gap-3 text-center">
              <Stat label="Totalt" value={stats.totalt} />
              <Stat label="Startade" value={stats.startade} />
              <Stat label="Avslutade" value={stats.avslutade} />
            </div>
          </div>
        </header>

        {error && <div className="mb-5 rounded-2xl bg-red-50 px-5 py-4 text-red-700 ring-1 ring-red-200">{error}</div>}

        <section className="grid gap-6 lg:grid-cols-[390px_1fr]">
          <aside className="glass-panel h-fit rounded-[1.75rem] p-6 shadow-soft ring-1 ring-white">
            <h2 className="mb-4 flex items-center gap-2 text-xl font-bold"><Plus size={22} /> {editingId ? 'Redigera uppgift' : 'Skapa ny uppgift'}</h2>
            <label className="mb-1 block text-sm font-semibold">Rubrik</label>
            <input className="mb-4 w-full rounded-2xl border border-slate-200 px-4 py-3 outline-none focus:ring-2 focus:ring-combi-cyan" value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} placeholder="Ex. Förbered demo" />

            <label className="mb-1 block text-sm font-semibold">Beskrivning</label>
            <textarea className="mb-4 min-h-28 w-full rounded-2xl border border-slate-200 px-4 py-3 outline-none focus:ring-2 focus:ring-combi-cyan" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} placeholder="Beskriv vad som ska göras" />

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="mb-1 block text-sm font-semibold">Prioritet</label>
                <select className="w-full rounded-2xl border border-slate-200 px-4 py-3" value={form.priority} onChange={e => setForm({ ...form, priority: e.target.value as Priority })}>
                  {priorities.map(p => <option key={p.value} value={p.value}>{p.label}</option>)}
                </select>
              </div>
              <div>
                <label className="mb-1 block text-sm font-semibold">Status</label>
                <select className="w-full rounded-2xl border border-slate-200 px-4 py-3" value={form.status} onChange={e => setForm({ ...form, status: e.target.value as TodoStatus })}>
                  {statuses.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
                </select>
              </div>
            </div>

            <button onClick={saveTodo} className="mt-5 w-full rounded-2xl bg-combi-blue px-5 py-3 font-bold text-white shadow-lg shadow-blue-900/20 transition hover:bg-blue-700">
              {editingId ? 'Spara ändringar' : 'Skapa uppgift'}
            </button>
            {editingId && <button onClick={() => { setEditingId(null); setForm(initialForm); }} className="mt-3 w-full rounded-2xl bg-slate-100 px-5 py-3 font-semibold text-slate-700">Avbryt</button>}
          </aside>

          <section>
            <div className="mb-5 grid gap-3 rounded-[1.75rem] bg-white p-4 shadow-soft md:grid-cols-[1fr_190px_190px]">
              <div className="relative">
                <Search className="absolute left-4 top-3.5 text-slate-400" size={20} />
                <input className="w-full rounded-2xl border border-slate-200 py-3 pl-12 pr-4 outline-none focus:ring-2 focus:ring-combi-cyan" value={search} onChange={e => setSearch(e.target.value)} placeholder="Sök på rubrik eller beskrivning" />
              </div>
              <select className="rounded-2xl border border-slate-200 px-4 py-3" value={statusFilter} onChange={e => setStatusFilter(e.target.value as TodoStatus | 'ALLA')}>
                <option value="ALLA">Alla statusar</option>
                {statuses.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
              </select>
              <select className="rounded-2xl border border-slate-200 px-4 py-3" value={priorityFilter} onChange={e => setPriorityFilter(e.target.value as Priority | 'ALLA')}>
                <option value="ALLA">Alla prioriteter</option>
                {priorities.map(p => <option key={p.value} value={p.value}>{p.label}</option>)}
              </select>
            </div>

            <div className="grid gap-5">
              {filteredTodos.map(todo => (
                <article key={todo.id} className="rounded-[1.75rem] bg-white p-6 shadow-soft ring-1 ring-slate-100 transition hover:-translate-y-0.5 hover:shadow-xl">
                  <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
                    <div>
                      <div className="mb-3 flex flex-wrap gap-2">
                        <Badge className={priorityClass(todo.priority)}>{todo.priorityText}</Badge>
                        <Badge className={statusClass(todo.status)}>{todo.statusText}</Badge>
                      </div>
                      <h3 className="text-2xl font-bold text-slate-900">{todo.title}</h3>
                      <p className="mt-2 max-w-3xl text-slate-600">{todo.description}</p>
                      <p className="mt-3 text-sm text-slate-400">Skapad: {formatDate(todo.createdAt)}</p>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {statuses.map(s => (
                        <button key={s.value} onClick={() => changeStatus(todo, s.value)} className={`rounded-full px-3 py-2 text-sm font-semibold ring-1 transition ${todo.status === s.value ? statusClass(s.value) : 'bg-slate-50 text-slate-600 ring-slate-200 hover:bg-slate-100'}`}>
                          {s.label}
                        </button>
                      ))}
                      <button onClick={() => startEdit(todo)} className="rounded-full bg-slate-900 px-3 py-2 text-sm font-semibold text-white">Redigera</button>
                      <button onClick={() => remove(todo.id)} className="rounded-full bg-red-50 px-3 py-2 text-sm font-semibold text-red-700 ring-1 ring-red-100"><Trash2 size={16} /></button>
                    </div>
                  </div>

                  <div className="mt-5 rounded-2xl bg-slate-50 p-4 ring-1 ring-slate-100">
                    <h4 className="mb-3 flex items-center gap-2 font-bold"><ClipboardList size={18} /> Historik</h4>
                    <div className="space-y-3">
                      {todo.history.map(entry => (
                        <div key={entry.id} className="flex gap-3 rounded-xl bg-white p-3 ring-1 ring-slate-100">
                          {entry.toStatus === 'AVSLUTAD' ? <CheckCircle2 className="mt-0.5 text-green-600" size={18} /> : <CircleDot className="mt-0.5 text-combi-blue" size={18} />}
                          <div>
                            <p className="font-medium text-slate-800">{entry.message}</p>
                            <p className="text-sm text-slate-500">{formatDate(entry.changedAt)}</p>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </article>
              ))}
            </div>
          </section>
        </section>
      </section>
    </main>
  );
}

function Badge({ children, className }: { children: React.ReactNode; className: string }) {
  return <span className={`rounded-full px-3 py-1 text-sm font-bold ring-1 ${className}`}>{children}</span>;
}

function Stat({ label, value }: { label: string; value: number }) {
  return (
    <div className="rounded-2xl bg-white/10 px-5 py-4 ring-1 ring-white/15">
      <div className="text-3xl font-bold">{value}</div>
      <div className="text-sm text-slate-200">{label}</div>
    </div>
  );
}
